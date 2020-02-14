<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<header class="flex_row" id="header">
	<svg>
       
      </svg>
      
	<a href="/"><img src="/img/youtube.png" id="logo" /></a>
	
	<form class="flex_row search_form">
		<div class="search_box">
			<input type="text" class="search_input" placeholder="검색" />
		</div>
		<button type="button" class="search_btn">
			<svg class="search_i">
            <path
					d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z" />
          </svg>
		</button>
	</form>
	<div class="header_btns flex_row">
		<a href="/studio"> <svg class="studio_btn">
          <path fill="#636e72"
					d="M17 10.5V7c0-.55-.45-1-1-1H4c-.55 0-1 .45-1 1v10c0 .55.45 1 1 1h12c.55 0 1-.45 1-1v-3.5l4 4v-11l-4 4zM14 13h-3v3H9v-3H6v-2h3V8h2v3h3v2z" />
        </svg>
		</a>

		<c:choose>
			<c:when test="${user.id >= 1}">
				
					<div class="avatar_wrapper" id="login_check">
						<img src="${user.avatar}" class="middle_avatar" />
					</div>
				
			</c:when>
			<c:otherwise>
				<a href="/auth/login">
					<div class="flex_row login_btn" id="login">
						<img src="/img/anomy.png" class="small_avatar" /> <span
							class="login_text">로그인</span>
					</div>
				</a>
			</c:otherwise>
		</c:choose>

	</div>
	      <div id="myAccount" class="flex_col">
        <div class="flex_row my_info">
          <img src="${user.avatar}" class="middle_avatar" />
          <div class="flex_col infos">
            <div>${user.username}</div>
            <div>${user.email}</div>
            <a href="#"><div class="avatar_link">프로필사진 수정</div c></a>
          </div>
        </div>
        <div class="setting">
          <div class="flex_row left info_set">
            <img src="/img/myChannel.svg" class="info_svg"/>
            <span>내 채널</span>
          </div>
          <div class="flex_row left info_set">
            <img src="/img/studio.svg" class="info_svg"/>
            <span>스튜디오</span>
          </div>
          <div class="flex_row left info_set">
            <img src="/img/logout.svg" class="info_svg"/>
            <span>로그아웃</span>
          </div>
        </div>
      </div>
</header>
<script src="/js/header.js"></script>