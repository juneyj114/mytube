package com.example.springsocial.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.springsocial.model.Subscribing;
import com.example.springsocial.model.User;
import com.example.springsocial.model.Video;
import com.example.springsocial.model.dto.VideoSaveRequestDto;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.service.SubscribingService;
import com.example.springsocial.service.VideoService;
import com.example.springsocial.util.Upload;

@RestController
public class VideoController {

	@Autowired
	private VideoService videoService;
	
	@Autowired
	private SubscribingService subService;
	
	@Autowired
	private Upload upload;
	
	@PostMapping("/video/upload")
	public String upload(@AuthenticationPrincipal UserPrincipal principal, @RequestParam("file") MultipartFile multipartFile) throws IOException {
		String result = upload.upload(multipartFile, principal.getId());
		Video video = new Video();
		video.setUrl(result);
		User user = new User();
		user.setId(principal.getId());
		video.setAuthor(user);
		videoService.save(video);
		return video.getUrl();
	}
	
	@PostMapping("/video/uploadDetail")
	public String uploadDetail(@RequestBody VideoSaveRequestDto dto) throws UnsupportedEncodingException {
		String url = dto.getUrl();
		url = URLDecoder.decode(url, "UTF-8");
		int startIndex = url.indexOf("upload")-1;
		url = url.substring(startIndex);
		dto.setUrl(url);
		

		videoService.update(dto);
		return "OK";
	}
	
	@GetMapping("/video/main")
	public List<Video> mainVideo(@PageableDefault(size = 16, direction = Direction.DESC, sort = "id") Pageable pageable){
		List<Video> videos = videoService.loadMainVideo(pageable);
		return videos;
	}
	
	@PostMapping("/video/countUp/{id}")
	public void countUp(@PathVariable Long id) throws Exception {
		videoService.readCountUp(id);
	}
	
	@GetMapping("/video/search")
	public List<Video> search(@RequestParam("word") String searchWord) {
		List<Video> videos = videoService.findBySearchWord(searchWord);
		return videos;
	}
	
	@PostMapping("/video/delete/{id}")
	public String delete(@PathVariable Long id) {
		String res = videoService.delete(id);
		if(res.equals("OK")) {
			return "OK";
		} else {
			return "FAIL";
		}
	}

}
