package com.example.springsocial.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.model.Subscribing;
import com.example.springsocial.model.User;
import com.example.springsocial.model.Video;
import com.example.springsocial.repository.SubscribingRepository;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.repository.VideoRepository;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.service.SubscribingService;
import com.example.springsocial.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private SubscribingService subService;
    
    @Transactional(readOnly = true)
    @GetMapping("/user")
    public User getCurrentUser(@AuthenticationPrincipal  UserPrincipal userPrincipal) {
    	User user = userService.findUserWithVideoAndSubById(userPrincipal.getId());
        return user;
                
    }
    
    @PostMapping("/sub/{id}")
    public String subscribe(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
    	System.out.println("1");
    	subService.save(principal.getId(), id);
    	return "OK";
    }

}
