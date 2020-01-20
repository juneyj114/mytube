package com.example.springsocial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.service.SubscribingService;

@RestController
public class SubController {

    @Autowired
    private SubscribingService subService;
    
    @PostMapping("/sub/{id}")
    public String subscribe(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
    	String res = subService.saveToggle(principal.getId(), id);
    	if(res == "SAVE") {
    		return "SAVE";
    	}
    	return "DELETE";
    }

}
