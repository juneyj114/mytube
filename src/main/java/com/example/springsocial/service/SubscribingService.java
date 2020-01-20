package com.example.springsocial.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsocial.model.Subscribing;
import com.example.springsocial.model.User;
import com.example.springsocial.repository.SubscribingRepository;

@Service
public class SubscribingService {

	@Autowired
	private SubscribingRepository subRepository;
	
	@Transactional
	public String saveToggle(Long subscriberId, Long subscribingId) {
		Subscribing savedSub = subRepository.findBySubscriberIdAndSubscribingId(subscriberId, subscribingId);
		if(savedSub != null) {
			subRepository.delete(savedSub);
			return "DELETE";
		} else {
			Subscribing sub = new Subscribing();
	    	User subscriber = new User();
	    	User subscribing = new User();
	    	subscriber.setId(subscriberId);
	    	subscribing.setId(subscribingId);
	    	sub.setUser(subscriber);
	    	sub.setSubscribing(subscribing);
	    	subRepository.save(sub);
	    	return "SAVE";
		}
	}
	
	@Transactional
	public List<Subscribing> findAllSubscribingId(Long id) {
		List<Subscribing> subs = subRepository.findAllSubscribingId(id);
		return subs;
	}

	@Transactional
	public boolean existsBySubscriberIdAndSubscribingId(Long subscriberId, Long subscribingId) {
		Subscribing sub = subRepository.findBySubscriberIdAndSubscribingId(subscriberId, subscribingId);
		if(sub != null) {
			return true;
		}
		return false;
	}
}
