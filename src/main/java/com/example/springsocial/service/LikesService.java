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
}
