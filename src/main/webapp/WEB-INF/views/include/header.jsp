<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script src="/webjars/sockjs-client/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/stomp.min.js"></script>
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
		
		<div class="notify_container" >	
          
        </div>
        
        
		
		<svg class="notifyIcon">
          <path
            fill="#636e72"
            d="M12 22c1.1 0 2-.9 2-2h-4c0 1.1.9 2 2 2zm6-6v-5c0-3.07-1.64-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.63 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z"
          ></path>
        </svg>
        <div class="notify_number_back">
          <span class="notify_number">5</span>
          </div>
		
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
				<img src="/img/myChannel.svg" class="info_svg" /> <span>내 채널</span>
			</div>
			<div class="flex_row left info_set">
				<img src="/img/studio.svg" class="info_svg" /> <span>스튜디오</span>
			</div>
			<div class="flex_row left info_set">
				<img src="/img/logout.svg" class="info_svg" /> <span>로그아웃</span>
			</div>
		</div>
	</div>
</header>
<script>
	const notifyContainer = document.querySelector(".notify_container");
	const notifyIcon = document.querySelector(".notifyIcon");

	const appendNotifyCard = (body) => {

		let notifyCard = document.createElement("div");
        notifyCard.classList.add("notify_card")
		let card = "";
		card += "	<img src='/img/anomy.png' class='notify_img' />";
		card += "	<div>";
		card += " 	<div>"+body.username+"에서 업로드한 동영상: "+body.title+"</div>";
		card += "		<div class='notify_time' >"+body.createDate+"</div>";
		card += "	</div>";
		notifyCard.innerHTML = card;
		notifyContainer.prepend(notifyCard);
		};

      let stompClient = null;
      let notifyNumber = document.querySelector(".notify_number");
      const connect = userId => {
        const socket = new SockJS("/mytube_websocket");
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
          console.log("Connected: " + frame);
          stompClient.subscribe("/topic/" + userId, function(pubMsg) {
        	  appendNotifyCard(JSON.parse(pubMsg.body));
        	  parseInt(notifyNumber.innerText)
        	  notifyNumber.innerText = parseInt(notifyNumber.innerText) + 1
          });
        });
      };
      connect("${user.id}");

  		let notifyOpen = false;
  		notifyNumber.innerText = 0;
  		notifyIcon.addEventListener("click", (e) => {
          if(!notifyOpen){
              notifyContainer.style.display="grid";
        	  notifyOpen = true;
           } else {
        	   notifyContainer.style.display="none";
        	   notifyContainer.innerHTML = "";
        	   notifyNumber.innerText = 0;
        	   notifyOpen = false;
               }
          
          });

      
    </script>
<script src="/js/header.js"></script>