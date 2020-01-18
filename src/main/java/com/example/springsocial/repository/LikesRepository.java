package com.example.springsocial.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springsocial.model.Likes;

public interface LikesRepository extends JpaRepository<Likes, Long>{
	
	List<Likes> findByVideoId(Long videoId);
	
	List<Likes> findByVideoIdAndIsLikeTrue(Long videoId);

	Likes findByVideoIdAndUserId(Long id, Long userId);
}