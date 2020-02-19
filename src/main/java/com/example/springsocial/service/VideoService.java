package com.example.springsocial.service;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsocial.model.Likes;
import com.example.springsocial.model.PubMsg;
import com.example.springsocial.model.Subscribing;
import com.example.springsocial.model.User;
import com.example.springsocial.model.Video;
import com.example.springsocial.model.dto.VideoSaveRequestDto;
import com.example.springsocial.repository.LikesRepository;
import com.example.springsocial.repository.SubscribingRepository;
import com.example.springsocial.repository.VideoRepository;
import com.example.springsocial.util.ConvertTimeStamp;

@Service
public class VideoService {

	@Autowired
	private VideoRepository videoRepository;
	
	@Autowired
	private LikesRepository likesRepository;
	
	@Autowired
	private ConvertTimeStamp convertTimeStamp;
	
	@Autowired
	private SubscribingRepository subscribingRepository;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@Transactional
	public void upload(Long id) {
		User user = new User();
		user.setId(id);
		Video video = new Video();
		video.setTitle("title");
		video.setContent("content");
		video.setAuthor(user);
		video.setIsPublic(true);
		video.setReadCount(0L);
		video.setUrl("hq720.jpg");
		videoRepository.save(video);
	}

	@Transactional
	public List<Video> loadMainVideo(Pageable pageable) {
		List<Video> videos = videoRepository.findByIsPublicTrue(pageable).getContent();
		videos = convertTimeStamp.convertTimeStamp(videos);
		return videos;
	}

	@Transactional
	public Video save(Video video) {
		video.setReadCount(0L);
		Video savedVideo = videoRepository.save(video);
		return savedVideo;
	}
	
	@Transactional
	public Video update(VideoSaveRequestDto dto) {
		Video video = videoRepository.findByUrl(dto.getUrl());
		video.setTitle(dto.getTitle());
		video.setContent(dto.getContent());
		video.setIsPublic(dto.isPub());
		sendMessage(video);
		return video;
	}

	@Transactional
	public Video findById(Long id) throws Exception {
		Video video = videoRepository.findById(id).orElseThrow(() -> new Exception("해당 동영상이 존재하지 않습니다."));
		return video;
	}
	
	@Transactional
	public Page<Video> findByAuthorId(Long id, Pageable pageable){
		Page<Video> videos = videoRepository.findByAuthorId(id, pageable);
		return videos;
	}
	
	@Transactional
	public void readCountUp(Long id) throws Exception {
		Video video = videoRepository.findById(id).orElseThrow(() -> new Exception("해당 영상을 찾을 수 없습니다."));
		video.setReadCount(video.getReadCount() + 1);
		
	}
	
	@Transactional
	public List<Video> findBySearchWord(String searchWord, Pageable pageable) {
		// (?=(.*단어.*){1,})
		String regexp = "";
		String[] splitWords = searchWord.split("\\s+");
		for (int i = 0; i < splitWords.length; i++) {
			regexp += "(?=(.*"+splitWords[i]+".*){1,})";
		} 
		List<Video> videos = videoRepository.findBySearchWord(regexp, pageable).getContent();
		videos = convertTimeStamp.convertTimeStamp(videos);
		return videos;
	}
	
	@Transactional
	public String delete(Long id) {
		Video video = videoRepository.findById(id).get();
		String url = video.getUrl();
		File file = new File("src/main/resources"+url);
		videoRepository.deleteById(id);
		return "OK";
	}
	
	@Transactional
	public Page<Video> findByAuthorIdAndSearch(Long id, String search, Pageable pageable) {
		String regexp = "(?=(.*"+search+".*){1,})";
		Page<Video> videos = videoRepository.findBySearchWord(regexp, pageable);
//		convertTimeStamp.convertTimeStamp(videos.getContent());
		return videos;
	}
	
	@Transactional
	public void sendMessage(Video video) {
		List<Subscribing> subs = subscribingRepository.findBySubscribingId(video.getAuthor().getId());
		video = convertTimeStamp.convertTimeStampOne(video);
		for (Subscribing sub : subs) {
			Long userId = sub.getUser().getId();
			PubMsg pubMsg = new PubMsg();
			pubMsg.setUsername(video.getAuthor().getUsername());
			pubMsg.setTitle(video.getTitle());
			pubMsg.setAvatar(video.getAuthor().getAvatar());
			pubMsg.setCreateDate(video.getDate());
			simpMessagingTemplate.convertAndSend("/topic/"+userId, pubMsg);
		}
	}
}
