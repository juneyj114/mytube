package com.example.springsocial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springsocial.model.PlayList;
import com.example.springsocial.model.Video;

public interface PlayListRepository extends JpaRepository<PlayList, Long> {

	Boolean existsByUserIdAndVideoId(Long userId, Long videoId);

	PlayList findByUserIdAndVideoId(Long userId, Long videoId);

	List<PlayList> findByUserId(Long userId);

	
}
