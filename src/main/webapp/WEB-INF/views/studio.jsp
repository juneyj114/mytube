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
      <img src="/img/hamberger.svg" class="small_icon" />
      <img src="/img/yt_studio_logo.svg" class="studio_logo" />
      <div class="input_box flex_row">
        <img src="/img/glass.svg" class="small_icon glass" />
        <input type="text" placeholder="채널에서 검색하기" class="input" />
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
        <div class="flex_row left nav_item">
          <img src="/img/dashboard.svg" />
          <span>대시보드</span>
        </div>
        <div class="flex_row left nav_item">
          <img src="/img/movie.svg" />
          <span>동영상</span>
        </div>
      </div>
    </nav>
    <section id="main">
      <div class="flex_col">
        <div class="big_text">채널 동영상</div>
        <div class="grid-7-col explain">
          <div>
            <input type="checkbox" />
          </div>
          <div>동영상</div>
          <div>공개 상태</div>
          <div>날짜</div>
          <div class="just_self_end">조회수</div>
          <div class="just_self_end">댓글</div>
          <div class="just_self_end">좋아요</div>
        </div>
        
        <c:forEach items="${user.videos}" var="video">
        <div class="grid-7-col contents_container">
          <div>
            <input type="checkbox" />
          </div>
          <div class="flex_row preview_container">
            <video src="${video.url}" class="preview_img" ></video>
            <div class="flex_col relative">
              <div class="title">${video.title}</div>
              <div class="content">${video.content}</div>
            </div>
          </div>
          <div>${video.isPublic? '공개' : '비공개'}</div>
          <div>${video.createDate}</div>
          <div class="just_self_end">${video.readCount}</div>
          <div class="just_self_end">미작성</div>
          <div class="just_self_end">${video.likeCount}</div>
        </div>
        </c:forEach>
        
        
        <div class="flex_row" id="paging">
          페이징
        </div>
      </div>
    </section>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="/js/studio.js"></script>

  </body>
</html>
