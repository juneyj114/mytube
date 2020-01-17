package com.example.springsocial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.springsocial.model.Subscribing;

public interface SubscribingRepository extends JpaRepository<Subscribing, Long> {

	@Query(value = "SELECT * FROM subscribing WHERE subscriberId=?1", nativeQuery = true)
	List<Subscribing> findAllSubscriberId(Long id);
	
	@Query(value = "SELECT * FROM subscribing WHERE subscribingId=?1", nativeQuery = true)
	List<Subscribing> findAllSubscribingId(Long id);
}
