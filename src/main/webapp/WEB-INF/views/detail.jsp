<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="ie=edge" />
<link rel="stylesheet" href="/css/main.css" />
<title>Document</title>

</head>
<body>
	<%@include file="include/header.jsp"%>
	<%@include file="include/nav.jsp"%>

	<section id="main">
		<div class="detail_container">
			<div class="present_video">
				<div class="video_container">
					<video id="video" width="1240" height="730" autoplay controls>
						<source src="${video.url}" type="video/mp4">
					</video>
					<div class="detail_title">${video.title}</div>
					<div class="flex_row detail_bottom gray_font">
						<div>조회수 ${video.readCount}회</div>
						<div class="point">·</div>
						<div>${video.createDate}</div>
						<div class="like_btns">
							<div class="like_btn" video-id="${video.id}">
								<c:choose>
									<c:when test="${video.like == 'LIKE'}">
										<img src="/img/liked.svg" class="small_avatar liked" />
									</c:when>
									<c:otherwise>
										<img src="/img/like.svg" class="small_avatar" />
									</c:otherwise>
								</c:choose>
								<div>${video.likeCount}</div>
							</div>
							<div class="unlike_btn" video-id="${video.id}">
								<c:choose>
									<c:when test="${video.like == 'UNLIKE'}">
										<img src="/img/unliked.svg" class="small_avatar unliked" />
									</c:when>
									<c:otherwise>
										<img src="/img/unlike.svg" class="small_avatar" />
									</c:otherwise>
								</c:choose>
								<div>${video.unLikeCount}</div>
							</div>
						</div>
					</div>
				</div>
				<div class="content_container">
					<div class="flex_row content_user">
						<img src="${video.author.avatar}" class="avatar_48" />
						<div>
							<div>${video.author.username}</div>
							<div class="gray_font font_13">구독자
								${video.author.subCount}명</div>
						</div>
						<button class="subscribe_btn">구독</button>
					</div>
					<div>${video.content}</div>
				</div>
				<!-- 라이브리 시티 설치 코드 -->
				<div id="lv-container" data-id="city"
					data-uid="MTAyMC80ODI3My8yNDc2OA==">
					<script type="text/javascript">
   (function(d, s) {
       var j, e = d.getElementsByTagName(s)[0];

       if (typeof LivereTower === 'function') { return; }

       j = d.createElement(s);
       j.src = 'https://cdn-city.livere.com/js/embed.dist.js';
       j.async = true;

       e.parentNode.insertBefore(j, e);
   })(document, 'script');
</script>
					<noscript>라이브리 댓글 작성을 위해 JavaScript를 활성화 해주세요</noscript>
				</div>
				<!-- 시티 설치 코드 끝 -->
			</div>
			<div class="next_video">다음 추천 비디오가 표시될 공간입니다.</div>
		</div>
	</section>
	<script src="/js/detail.js"></script>
</body>
</html>
