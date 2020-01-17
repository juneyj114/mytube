package com.example.springsocial.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Likes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	
	@ManyToOne
	@JoinColumn(name = "userId", columnDefinition = "BIGINT")
	public User user;
	
	@ManyToOne
	@JoinColumn(name = "videoId", columnDefinition = "BIGINT")
	public Video video;
	
	public Boolean isLike;
}
