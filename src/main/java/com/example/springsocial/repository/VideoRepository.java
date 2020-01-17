package com.example.springsocial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.springsocial.model.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {

	@Query(value = "SELECT * FROM video WHERE authorId=?1", nativeQuery = true)
	List<Video> findAllAuthorId(Long id);

	Video findByUrl(String url);

	List<Video> findByAuthorId(Long id);
	
	@Query(value = "select * from video where concat(title,content) regexp ?", nativeQuery = true)
	List<Video> findBySearchWord(String regexp);
}
