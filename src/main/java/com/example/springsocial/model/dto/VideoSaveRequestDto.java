package com.example.springsocial.model.dto;

import lombok.Data;

@Data
public class VideoSaveRequestDto {

	private String title;
	private String content;
	private boolean pub;
	private String url;
}
