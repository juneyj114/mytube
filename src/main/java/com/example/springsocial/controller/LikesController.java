package com.example.springsocial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsocial.model.Likes;
import com.example.springsocial.model.User;
import com.example.springsocial.model.Video;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.service.LikesService;

@RestController
public class LikesController {
	
	@Autowired
	private LikesService likesService;
	
	@PostMapping("/like/{id}/{isLike}")
	public String like(@PathVariable Long id, @PathVariable boolean isLike, @AuthenticationPrincipal UserPrincipal userPrincipal) {
		Likes like = new Likes();
		User user = new User();
		Video video = new Video();
		
		user.setId(userPrincipal.getId());
		video.setId(id);
		
		like.setUser(user);
		like.setVideo(video);
		
		if(isLike) {
			like.setIsLike(true);			
		} else {
			like.setIsLike(false);
		}
		likesService.saveToggle(like);
		return "OK";
	}

}
