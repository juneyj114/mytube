package com.example.springsocial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsocial.model.PlayList;
import com.example.springsocial.model.Video;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.service.PlayListService;

@RestController
public class PlayListController {

	@Autowired
	private PlayListService playListService; 
	
	@GetMapping("/playList")
	public List<PlayList> all(@AuthenticationPrincipal UserPrincipal userPrincipal) {
		if(userPrincipal == null) {
			return null;
		}
		List<PlayList> lists = playListService.findByUserId(userPrincipal.getId());
		return lists;
	}
	
	@PostMapping("/playList/create/{id}")
	public String save(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long id) {
		if(userPrincipal == null) {
			return "FAIL";
		}
		String res = playListService.save(userPrincipal.getId(), id);
		if(res.equals("OK")) {
			return "OK";
		} else if(res.equals("EXIST")){
			return "EXIST";
		} else {
			return "FAIL";
		}
	}
	
	@PostMapping("/playList/delete/{id}")
	public String delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long id) {
		if(userPrincipal == null) {
			return "FAIL";
		}
		String res = playListService.delete(userPrincipal.getId(), id);
		if(res.equals("OK")) {
			return "OK";
		} else {
			return "FAIL";
		}
	}
}
