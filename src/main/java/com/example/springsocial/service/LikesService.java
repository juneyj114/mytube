package com.example.springsocial.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsocial.model.Likes;
import com.example.springsocial.repository.LikesRepository;

@Service
public class LikesService {

	@Autowired
	private LikesRepository likesRepository;
	
	@Transactional
	public List<Likes> findByVideoIdAndLike(Long videoId) {
		List<Likes> likes = likesRepository.findByVideoIdAndIsLikeTrue(videoId);
		return likes;
	}

	@Transactional
	public List<Likes> findByVideoId(Long id) {
		List<Likes> likes = likesRepository.findByVideoId(id);
		return likes;
	}
	
	@Transactional
	public void saveToggle(Likes like) {
		Likes savedLike = likesRepository.findByVideoIdAndUserId(like.getVideo().getId(), like.getUser().getId());
		if(savedLike != null) {
			if(savedLike.isLike == like.isLike) {
				likesRepository.delete(savedLike);
			} else {
				savedLike.setIsLike(like.isLike);
			}
		} else {
			likesRepository.save(like);
		}
	}
	
	@Transactional
	public Likes findByVideoIdAndUserId(Long id, Long userId) {
		Likes like = likesRepository.findByVideoIdAndUserId(id, userId);
		return like;
	}
}
