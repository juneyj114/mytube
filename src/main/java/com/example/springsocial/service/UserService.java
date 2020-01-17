package com.example.springsocial.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.model.Subscribing;
import com.example.springsocial.model.User;
import com.example.springsocial.model.Video;
import com.example.springsocial.repository.SubscribingRepository;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.repository.VideoRepository;

@Service
public class UserService {

	@Autowired
    private UserRepository userRepository;
    
    @Autowired
    private VideoRepository videoRepository;
    
    @Autowired
    private SubscribingRepository subRepository;
    
    @Transactional
    public User findUserWithVideoAndSubById(Long id) {
    	User user = userRepository.findById(id)
    			.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    	List<Video> videos = videoRepository.findAllAuthorId(id);
    	user.setVideos(videos);
    	
    	List<Subscribing> subs = subRepository.findAllSubscriberId(id);
    	user.setSubscribing(subs);
        return user;
    }
    
    @Transactional
    public User findUserWithSubById(Long id){
    	User user = userRepository.findById(id)
    			.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    	
    	List<Subscribing> subs = subRepository.findAllSubscriberId(id);
    	user.setSubscribing(subs);
    	
    	return user;
    }
}
