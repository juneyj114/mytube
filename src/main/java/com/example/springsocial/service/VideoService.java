package com.example.springsocial.service;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsocial.model.Likes;
import com.example.springsocial.model.User;
import com.example.springsocial.model.Video;
import com.example.springsocial.model.dto.VideoSaveRequestDto;
import com.example.springsocial.repository.LikesRepository;
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
		List<Video> videos = videoRepository.findAll(pageable).getContent();
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
	public List<Video> findBySearchWord(String searchWord) {
		// (?=(.*단어.*){1,})
		String regexp = "";
		String[] splitWords = searchWord.split("\\s+");
		for (int i = 0; i < splitWords.length; i++) {
			regexp += "(?=(.*"+splitWords[i]+".*){1,})";
		} 
		List<Video> videos = videoRepository.findBySearchWord(regexp);
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
}
