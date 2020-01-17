package com.example.springsocial.security.oauth2.user;

import java.nio.file.Files;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.springsocial.util.Upload;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
	
	public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getUsername() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }
}