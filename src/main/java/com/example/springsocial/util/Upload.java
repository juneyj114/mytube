package com.example.springsocial.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.springsocial.security.UserPrincipal;

@Component
public class Upload {
	
	public String upload(MultipartFile multipartFile, Long id) throws IOException {
		UUID uuid = UUID.randomUUID();
		String uuidFilename = uuid + "_" + multipartFile.getOriginalFilename();
		InputStream ins = multipartFile.getInputStream();
		Long oriSize = multipartFile.getSize();
		int splitSize = 1024*1024*100; // 100MB
		int splitCount =  (int)(oriSize / splitSize); // 210MB를 쪼개면 Count는 2
		int splitRest = (int)(oriSize % splitSize);	// Rest는 10MB
		
		String folderPath = "src/main/resources/upload/"+id;
		File folder = new File(folderPath);
		if(!folder.exists()) {
			folder.mkdir();
			System.out.println(folderPath+" 폴더가 생성되었습니다.");
		}
		String filePath = folderPath+"/"+uuidFilename;
		File file = new File(filePath);
		FileOutputStream out = new FileOutputStream(file);
		
		try {
			for (int i = 0; i < splitCount; i++) {
				byte[] bytes = new byte[splitSize];
				int len = splitSize;
				int header = 0;
				while(len > 0) {
					int read = ins.read(bytes, header, len);
					header += read;
					len -= read;
				}
				out.write(bytes);
			}
			byte[] bytes = new byte[splitRest];
			int len = splitRest;
			int header = 0;
			while(len > 0) {
				int read = ins.read(bytes, header, len);
				header += read;
				len -= read;
			}
			out.write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			out.close();
			new Exception("업로드 중 오류가 발생했습니다.");
		}
		out.close();
		return "/upload/"+id+"/"+uuidFilename;
		
	}
	
	public String uploadAvatar(String path, Long id) throws Exception {
		URI u = URI.create(path);
		URL url = u.toURL(); 
		URLConnection conn = url.openConnection();
		InputStream inputStream = conn.getInputStream();
		String folderPath = "src/main/resources/upload/"+id;
		File folder = new File(folderPath);
		if(!folder.exists()) {
			folder.mkdir();
			System.out.println(folderPath+" 폴더가 생성되었습니다.");
		}
		UUID uuid = UUID.randomUUID();
		String uuidFilename = uuid + "_avatar.jpg";
		String filePath = folderPath+"/"+uuidFilename;
		File file = new File(filePath);		
		FileOutputStream outputSteam = new FileOutputStream(file);
		byte[] bytes = new byte[conn.getContentLength()];
		int len = bytes.length;
		int header = 0;
		while(len > 0) {
			int read = inputStream.read(bytes, header, len);
			header += read;
			len -= read;
		}
		outputSteam.write(bytes);
		return "/upload/"+id+"/"+uuidFilename;
	}

}
