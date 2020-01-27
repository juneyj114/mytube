<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="ie=edge" />
<title>Document</title>
<link rel="stylesheet" href="/css/main.css" />
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
</head>
<body>
	<%@include file="include/header.jsp" %>
	<%@include file="include/nav.jsp" %>
	
	<section id="main">
		<div class="title big_text">최신 동영상</div>
		<div class="grid-col-4" id="main_video"></div>
	</section>
	<section id="mini_player" >
		<div class="player_view" style="border-bottom: 1px solid">
			<video src="/mv/testVideo.mp4" class="player_video" controls></video>
			<div class="curtain">
				<img src="/img/exitw.svg" class="small_avatar close_player" />
			</div>
		</div>
		<div class="player_detail" >
			<div>
				<div class="player_title">제목</div>
				<span>재생목록</span>
				<span class="player_count"></span>
				<span>/</span>
				<span class="player_total_count"></span>
			</div>
			<img src="img/up.svg" class="small_avatar more" onclick="more()"/>
		</div>
		<div class="player_list" >
		</div>
	</section>
	<script src="/js/main2.js"></script>
	<script src="/js/mplayer.js"></script>
</body>
</html>
