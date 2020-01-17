package com.example.springsocial.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.IOUtils;

public class Test {

	public static void main(String[] args) throws Exception {
		
		URI u = URI.create("https://lh5.googleusercontent.com/-ZNTrY2lk4uE/AAAAAAAAAAI/AAAAAAAAAAA/ACHi3rdaKIMXZHHSPS7eFB4T2s337RqfXQ/photo.jpg");
		URL url = u.toURL(); 
		URLConnection conn = url.openConnection();
		InputStream inputStream = conn.getInputStream();
		File file = new File("src/main/resources/upload/test.jpg");		
		FileOutputStream outputSteam = new FileOutputStream(file);
		byte[] bytes = new byte[conn.getContentLength()];
		int len = bytes.length;
		int header = 0;
		while(len > 0) {
			System.out.println(len);
			int read = inputStream.read(bytes, header, len);
			header += read;
			len -= read;
		}
		outputSteam.write(bytes);
	}
}
