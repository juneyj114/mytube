package com.example.springsocial.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsocial.model.PlayList;
import com.example.springsocial.model.User;
import com.example.springsocial.model.Video;
import com.example.springsocial.repository.PlayListRepository;

@Service
public class PlayListService {

	@Autowired
	private PlayListRepository playListRepository;
	
	@Transactional
	public String save(Long userId, Long videoId) {
		PlayList playList = new PlayList();
		playList.setUser(User.builder().id(userId).build());
		playList.setVideo(Video.builder().id(videoId).build());
		Boolean existsVideo = playListRepository.existsByUserIdAndVideoId(userId, videoId);
		if(existsVideo) {
			return "EXIST";
		}
		playListRepository.save(playList);
		System.out.println(playList.getId());
		if(playList.getId() > 0L) {
			return "OK";
		} else {
			return "FAIL";
		}
		
	}

	@Transactional
	public String delete(Long userId, Long videoId) {
		PlayList playList = playListRepository.findByUserIdAndVideoId(userId, videoId);
		if(playList != null) {
			playListRepository.delete(playList);
			return "OK";
		} else {
			return "FAIL";
		}
	}
	
	@Transactional
	public List<PlayList> findByUserId(Long userId) {
		List<PlayList> lists = playListRepository.findByUserId(userId);
		return lists;
	}
}
