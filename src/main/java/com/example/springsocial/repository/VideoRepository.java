package com.example.springsocial.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.springsocial.model.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {

	@Query(value = "SELECT * FROM video WHERE authorId=?1", nativeQuery = true)
	List<Video> findAllAuthorId(Long id);

	Video findByUrl(String url);

	Page<Video> findByAuthorId(Long id, Pageable pageable);
	
	@Query(value = "select * from video where concat(title,content) regexp ?", nativeQuery = true)
	Page<Video> findBySearchWord(String regexp, Pageable pageable);

	Page<Video> findByIsPublicTrue(Pageable pageable);
}
