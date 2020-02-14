<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>Document</title>
    <link rel="stylesheet" href="/css/studio.css" />
  </head>
  <body>
    <!-- 업로드 팝업창 시작 -->
    <div class="upload">
      <div class="upload_popup">
        <div class="upload_head">
          <div>동영상 업로드</div>
          <img src="/img/exit.svg" class="small_icon right" id="exit" />
        </div>

        <div class="upload_file">
          <label for="file" class="file_label">파일 선택</label>
          <input type="file" name="" id="file" />
        </div>

        </div>
      </div>
    </div>
    <!-- 업로드 팝업창 끝 -->
    <header class="flex_row" id="header">
	  <a href="/studio"><img src="/img/yt_studio_logo.svg" class="studio_logo" /></a>
      <div class="input_box flex_row">
        <img src="/img/glass.svg" class="small_icon glass" />
        <form action="/studio/search" method="get">
        	<input name="search" type="text" placeholder="채널에서 검색하기" class="input search_input" />
        </form>
        <div class="search_result hide">
        </div>
      </div>
      <div class="flex_row header_btns">
        <div class="flex_row" id="upload">
          <img src="/img/upload.svg" class="small_icon" />
          <span class="upload_text">만들기</span>
        </div>
        <img src="${user.avatar}" class="middle_avatar" />
      </div>
    </header>
    <nav id="nav">
      <div class="nav_item_container flex_col">
        <img
          src="${user.avatar}"
          class="big_avatar align_self_center"
          id="nav_avatar"
        />
        <a href="/">
	        <div class="flex_row left nav_item">
	          <img src="/img/home.svg" />
	          <span>홈</span>
	        </div>
        </a>
        <a href="/studio/dashboard">
	        <div class="flex_row left nav_item">
	          <img src="/img/dashboard.svg" />
	          <span>대시보드</span>
	        </div>
        </a>
        <a href="/studio">
	        <div class="flex_row left nav_item">
	          <img src="/img/movie.svg" />
	          <span>동영상</span>
	        </div>
        </a>
      </div>
    </nav>
    <section id="main">
      <div class="flex_col">
        <div class="big_text">채널 동영상</div>
        <div class="delete_many">선택한 영상 삭제하기</div>
        <div class="grid-7-col explain">
          <div>
            <input type="checkbox" id="all_check"/>
          </div>
          <div>동영상</div>
          <div>공개 상태</div>
          <div>날짜</div>
          <div class="just_self_end">조회수</div>
          <div class="just_self_end">댓글</div>
          <div class="just_self_end">좋아요</div>
        </div>
        
        <form action="/video/delete/many" method="POST">
        <c:forEach items="${user.videos}" var="video">
        <div class="grid-7-col contents_container" id="video_container" video-id="${video.id}">
          <div>
            <input type="checkbox" value="${video.id}" class="check"/>
          </div>
          <div class="flex_row preview_container">
            <video src="${video.url}" class="preview_img" ></video>
            <div id="btn_change" >
	            <div class="flex_col absolute" >
	              <div class="title">${video.title}</div>
	              <div class="content">${video.content}</div>
	            </div>
	            <div class="flex_row absolute hide" id="mod_btn">
	            	<img src="/img/write.svg" class="small_icon write" video-id="${video.id}" >
	            	<img src="/img/delete.svg" class="small_icon delete" video-id="${video.id}" >
	            </div>
            </div>
          </div>
          <div>${video.isPublic? '공개' : '비공개'}</div>
          <div>
          <fmt:formatDate pattern="yy.MM.dd hh:mm" value="${video.createDate}" />
          </div>
          <div class="just_self_end">${video.readCount}</div>
          <div class="just_self_end">미작성</div>
          <div class="just_self_end">${video.likeCount}</div>
        </div>
        </c:forEach>
        </form>
        
        <div class="flex_row" id="paging">
          <a href="/studio?page=0"><img src="/img/first.svg" class="middle_avatar"/></a>
          <a href="/studio?page=${currentPage eq 0 ? 0 : currentPage-1}"><img src="/img/left.svg" class="middle_avatar"/></a>
          <a href="/studio?page=${isLast ? currentPage : currentPage+1}"><img src="/img/right.svg" class="middle_avatar"/></a>
          <a href="/studio?page=${lastPage}"><img src="/img/last.svg" class="middle_avatar"/></a>
        </div>
      </div>
    </section>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="/js/underscore-min.js"></script>
    <script src="/js/studio.js"></script>

  </body>
</html>
