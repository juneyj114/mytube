package com.example.springsocial.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PubMsg {
	
	private String username;
	private String title;
	private String avatar;
	private String createDate;
}
