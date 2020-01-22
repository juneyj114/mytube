package com.example.springsocial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.springsocial.model.Likes;
import com.example.springsocial.model.Subscribing;
import com.example.springsocial.model.User;
import com.example.springsocial.model.Video;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.service.LikesService;
import com.example.springsocial.service.SubscribingService;
import com.example.springsocial.service.UserService;
import com.example.springsocial.service.VideoService;
import com.example.springsocial.util.LikeStatus;
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
	public String home(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
		if (userPrincipal != null) {
			User user = userService.findUserWithSubById(userPrincipal.getId());
			model.addAttribute("user", user);
		}
		return "main";
	}

	@GetMapping("/studio")
	public String studio(@AuthenticationPrincipal UserPrincipal userPrincipal, 
						@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
						@RequestParam("page") int page,
						Model model) {
		User user = userService.findUserWithSubById(userPrincipal.getId());
		Page<Video> videos = videoService.findByAuthorId(user.getId(), pageable);
		user.setVideos(videos.getContent());
		model.addAttribute("isLast", videos.isLast());
		model.addAttribute("currentPage", page);
		model.addAttribute("lastPage", videos.getTotalPages()-1);

		for (Video video : videos) {
			List<Likes> likes = likesService.findByVideoIdAndLike(video.getId());
			video.setLikeCount(likes.size());
			String title = video.getTitle();
			String content = video.getContent();
			if (title.length() > 13) {
				title = title.substring(0, 14) + "...";
				video.setTitle(title);
			}
			if (content.length() > 30) {
				content = content.substring(0, 31) + "...";
				video.setContent(content);
			}
		}
		model.addAttribute("user", user);
		return "studio";
	}

	@GetMapping("/auth/login")
	public String login() {
		return "auth/loginForm";
	}

	@GetMapping("/video/{id}")
	public String detail(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal userPrincipal, Model model)
			throws Exception {
		Video video = videoService.findById(id);
		User author = video.getAuthor();
		List<Subscribing> subs = subService.findAllSubscribingId(author.getId());
		int subCount = subs.size();
		author.setSubCount(subCount);
		List<Likes> likes = likesService.findByVideoId(id);
		int likeCount = 0;
		int unLikeCount = 0;
		for (Likes like : likes) {
			if (like.isLike) {
				likeCount++;
			} else {
				unLikeCount++;
			}
		}
		video.setLikeCount(likeCount);
		video.setUnLikeCount(unLikeCount);
		if (userPrincipal != null) {
			Likes like = likesService.findByVideoIdAndUserId(id, userPrincipal.getId());
			if (like == null) {
				video.setLike(LikeStatus.NOTHING);
			} else if (like.isLike) {
				video.setLike(LikeStatus.LIKE);
			} else {
				video.setLike(LikeStatus.UNLIKE);
			}
			User user = userService.findUserWithSubById(userPrincipal.getId());
			model.addAttribute("user", user);
		}
		boolean existSub = subService.existsBySubscriberIdAndSubscribingId(userPrincipal.getId(), author.getId());
		if (existSub) {
			video.setSub(true);
		}
		model.addAttribute("video", video);

		videoService.readCountUp(id);
		return "detail";
	}
}
