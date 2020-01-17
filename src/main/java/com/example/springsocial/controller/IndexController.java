package com.example.springsocial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.springsocial.model.Likes;
import com.example.springsocial.model.Subscribing;
import com.example.springsocial.model.User;
import com.example.springsocial.model.Video;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.service.LikesService;
import com.example.springsocial.service.SubscribingService;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.VideoService;
import com.example.springsocial.util.Upload;

@Controller
public class IndexController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LikesService likesService;
	
	@Autowired
	private VideoService videoService;
	
	@Autowired
	private SubscribingService subService;
	
	@GetMapping("/")
    public String home(@AuthenticationPrincipal  UserPrincipal userPrincipal, Model model) {
		if(userPrincipal != null) {
			User user = userService.findUserWithSubById(userPrincipal.getId());
			model.addAttribute("user",user);
		}
        return "main";
    }
	
	@GetMapping("/studio")
	public String studio(@AuthenticationPrincipal  UserPrincipal userPrincipal, Model model) {
		User user = userService.findUserWithSubById(userPrincipal.getId());
		List<Video> videos = videoService.findByAuthorId(user.getId());
		user.setVideos(videos);
		for (Video video : videos) {
			List<Likes> likes = likesService.findByVideoIdAndLike(video.getId());
			video.setLikeCount(likes.size());
			String title = video.getTitle();
			String content = video.getContent();
			if(title.length() > 13) {
				title = title.substring(0, 14)+"...";
				video.setTitle(title);
			}
			if(content.length() > 30) {
				content = content.substring(0, 31)+"...";
				video.setContent(content);
			}
		}
		model.addAttribute("user",user);
		return "studio";
	}
	
	@GetMapping("/auth/login")
	public String login() {
		return "auth/loginForm";
	}
	
	@GetMapping("/video/{id}")
	public String detail(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal, Model model) throws Exception {
		Video video = videoService.findById(id);
		User author = video.getAuthor();
		List<Subscribing> subs = subService.findAllSubscribingId(author.getId());
		int subCount = subs.size();
		author.setSubCount(subCount);
		model.addAttribute("video", video);
		
		if(userPrincipal != null) {
			User user = userService.findUserWithSubById(userPrincipal.getId());
			model.addAttribute("user",user);
		}
		videoService.readCountUp(id);
		return "detail";
	}
}
