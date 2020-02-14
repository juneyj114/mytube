package com.example.springsocial.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.springsocial.model.Video;

@Component
public class ConvertTimeStamp {

	public Video convertTimeStampOne(Video video) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		Timestamp timeStampDate = video.getCreateDate();
		long dif = System.currentTimeMillis() - timeStampDate.getTime();
		if (dif >= 86400000) { // 86400000밀리초 = 24시간
			String formatDate = format.format(timeStampDate);
			String date = formatDate;
			video.setDate(date);
		} else if (dif >= 3600000) { // 3600000밀리초 = 1시간
			int hourDate = (int) dif / 3600000;
			String date = hourDate + "시간 전";
			video.setDate(date);
		} else {
			int minDate = (int) dif / 60000;
			String date = minDate + "분 전";
			video.setDate(date);
		}

		return video;
	}

	public List<Video> convertTimeStamp(List<Video> videos) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		for (Video video : videos) {
			Timestamp timeStampDate = video.getCreateDate();
			long dif = System.currentTimeMillis() - timeStampDate.getTime();
			if (dif >= 86400000) { // 86400000밀리초 = 24시간
				String formatDate = format.format(timeStampDate);
				String date = formatDate;
				video.setDate(date);
			} else if (dif >= 3600000) { // 3600000밀리초 = 1시간
				int hourDate = (int) dif / 3600000;
				String date = hourDate + "시간 전";
				video.setDate(date);
			} else {
				int minDate = (int) dif / 60000;
				String date = minDate + "분 전";
				video.setDate(date);
			}
		}
		return videos;
	}
}
