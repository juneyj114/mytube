package com.example.springsocial.security.oauth2;

import com.example.springsocial.exception.OAuth2AuthenticationProcessingException;
import com.example.springsocial.model.AuthProvider;
import com.example.springsocial.model.Role;
import com.example.springsocial.model.User;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.security.oauth2.user.OAuth2UserInfo;
import com.example.springsocial.security.oauth2.user.OAuth2UserInfoFactory;
import com.example.springsocial.util.Upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private Upload upload;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) throws Exception {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }
    
    @Transactional
    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) throws Exception {
        User user = new User();
        
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        user.setProvider(AuthProvider.valueOf(registrationId));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setUsername(oAuth2UserInfo.getUsername());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setAvatar(oAuth2UserInfo.getImageUrl());
        user.setRole(Role.valueOf("USER")); // 추가
        System.out.println(registrationId);
        userRepository.save(user);
        if(registrationId.equals("google")) {
        	String url = upload.uploadAvatar(user.getAvatar(), user.getId());
        	user.setAvatar(url);
        	userRepository.saveAndFlush(user);
        }
        return user;
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) throws Exception {
        existingUser.setUsername(oAuth2UserInfo.getUsername());
        existingUser.setAvatar(oAuth2UserInfo.getImageUrl());
//        if(existingUser.getProvider().name() == "google") {
//        	String url = upload.uploadAvatar(existingUser.getAvatar(), existingUser.getId());
//        	existingUser.setAvatar(url);
//        }
        return userRepository.save(existingUser);
    }

}