let page = 0;
let isMain = true;

$(window).scroll(async function() {
  if (
    isMain &&
    $(window).scrollTop() == $(document).height() - $(window).height()
  ) {
    const videos = await loadMainVideo();
    appendMainVideo(videos);
    page++;
  }
});

const miniInit = async () => {
	document.querySelector(".player_list").innerHTML="";
	const miniVideos = await loadMiniVideo();
	  miniVideos.forEach(ele => {
		 const video = ele.video;
		 const {author} = ele.video;
		 const miniVideo = makeMiniVideo(video, author);
		 appendMiniVideoToPlayer(miniVideo);
		 registerDelete(miniVideo);
	  });
	  if(localStorage.getItem("currentList") === null){
		  localStorage.setItem("currentList", miniVideos[0].video.id);	  
	  }
	  const currentList = localStorage.getItem("currentList");
	  const player_video = document.querySelector(".player_video");
	  player_video.addEventListener("ended", (e) => {
		  const currentList = localStorage.getItem("currentList");
		  
		  const listDetails = document.querySelectorAll(".player_list_detail");
		  let count = 0;
		  listDetails.forEach(detail => {
			  if(detail.getAttribute("video-id") !== currentList){
				  count++;
			  } else {
				  count++;
				  miniVideoClickEvent(listDetails[count]);
			  }
			  });
		  
	  });
	  const player_view = document.querySelector(".player_view");
	  player_view.addEventListener("mouseenter", (e) => {
		  document.querySelector(".curtain").style.display = "flex";
	  });
	  player_view.addEventListener("mouseleave", (e) => {
		  document.querySelector(".curtain").style.display = "none";
	  });
	  const fetchVideo = await fetch(`/video/detail/${currentList}`)
	  const video = await fetchVideo.json();
	  player_video.src = video.url;
	  const listDetails = document.querySelectorAll(".player_list_detail");
	  const totalCount = listDetails.length;
	  document.querySelector(".player_total_count").innerText = totalCount;
	  let count = 1;
	  listDetails.forEach(detail => {
		  if(detail.getAttribute("video-id") !== currentList){
			  count ++;
		  } else {
			  document.querySelector(".player_count").innerText = count;
			  return;
		  }
	  });
		const checks = document.querySelectorAll(".play_check");
		checks.forEach(check => {
			if(check.getAttribute("video-id") === currentList){
				check.innerText = "▶";
			} else {
				check.innerText = "";
			}
		});
		const closePlayer = document.querySelector(".close_player");
		const player = document.querySelector("#mini_player");
		closePlayer.addEventListener("click", ()=>{
			player.style.display = "none";
		});
		
};

const loadMainVideo = async () => {
  const fetchData = await fetch(`/video/main?page=${page}`);
  const videos = await fetchData.json();
  return videos;
};

const loadMiniVideo = async () => {
	const fetchData = await fetch("/playList");
	const videos = await fetchData.json();
	return videos;
}

const makeMiniVideo = (video, author) => {
	miniVideo = document.createElement("div");
	miniVideo.classList.add("player_list_detail");
	miniVideo.setAttribute("video-id", video.id);
	div = ``;
	
	div += `	<div class="play_check" video-id="${video.id}"></div>`;
	div += `	<div class="list_mv_container">`;
	div += `		<video src="${video.url}" class="list_mv" ></video>`;
	div += `	</div>`;
	div += `	<div class="player_list_info">`;
	div += `		<div class="player_title">${video.title}</div>`;
	div += `		<div>${author.username}</div>`;
	div += `	</div>`;
	div += `	<div class="remove_list_detail">`;
	div += `		<img src="/img/delete.svg" video-id="${video.id}"/>`;
	div += `	</div>`;
	miniVideo.innerHTML = div;
	return miniVideo;
};

const miniVideoClickEvent = (miniVideo) => {

	videoId = miniVideo.getAttribute("video-id");
	localStorage.setItem("currentList", videoId);
	fetch(`/video/detail/${videoId}`)
		.then(res => res.json())
		.then(res => {
			const titleDiv = document.querySelector(".player_title");
			titleDiv.innerText = res.title;
		});
	let count = 1;
	const listDetails = document.querySelectorAll(".player_list_detail");
	  listDetails.forEach(detail => {
		  if(detail.getAttribute("video-id") !== videoId){
			  count ++;
		  } else {
			  document.querySelector(".player_count").innerText = count;
			  return;
		  }});
	const checks = document.querySelectorAll(".play_check");
	checks.forEach(check => {
		if(check.getAttribute("video-id") === videoId){
			check.innerText = "▶";
		} else {
			check.innerText = "";
		}
	});
	const src = miniVideo.querySelector("video").getAttribute("src");
	const player_video = document.querySelector(".player_video");
	player_video.src = src;
	player_video.addEventListener("loadeddata", (e) => {
		e.target.play();
	});
};

const appendMiniVideoToPlayer = (miniVideo) => {
	const player_list = document.querySelector(".player_list");
	player_list.append(miniVideo);
	miniVideo.addEventListener("click", (e) => {miniVideoClickEvent(miniVideo)});
}

const registerDelete = (miniVideo) => {
		const remove = miniVideo.querySelector(".remove_list_detail > img")
		miniVideo.addEventListener("mouseenter", (e) => {
			remove.parentNode.style.display = "block";
		});
		miniVideo.addEventListener("mouseleave", (e) => {
			remove.parentNode.style.display = "none";
		});
		remove.addEventListener("click", (e) => {
			if(confirm("해당 영상을 재생 목록에서 제외합니다.")){
				const videoId = e.target.getAttribute("video-id");
				fetch(`/playList/delete/${videoId}`, {method:"post"})
					.then(res => res.text())
					.then(res => {if(res === "OK"){
						miniVideo.remove();
						if(localStorage.getItem("currentList") === videoId){
							localStorage.removeItem("currentList");
						}
						miniInit();
					} else {
						alert("다시 시도해 주세요.");
					}});
			};
		});
};

const appendMainVideo = videos => {

  const mainVideo = document.querySelector("#main_video");
  videos.forEach(video => {
    const { author } = video;
    const cardDiv = document.createElement("div");
    cardDiv.classList.add("big_card", "flex_col");
    cardDiv.innerHTML = makeVideoBox(video, author);
    cardDiv.addEventListener("mouseenter", (e) => {
    	e.target.querySelector(".playList_save").style.display="flex";
    });
    cardDiv.addEventListener("mouseleave", (e) => {
    	e.target.querySelector(".playList_save").style.display="none";
    });
    playListIcon = cardDiv.querySelector(".playList_save");
    playListText = cardDiv.querySelector(".playList_save > span");
    playListIcon.addEventListener("mouseenter", (e) => {	
    	e.target.childNodes[1].style.display = "block";
    });
    playListIcon.addEventListener("mouseleave", (e) => {
    	e.target.childNodes[1].style.display = "none";
    });
    playListIcon.addEventListener("click", (e) => {	
    	e.preventDefault();
    	const player = document.querySelector("#mini_player");
    	player.style.display = "grid";
    	const videoId = e.target.parentNode.getAttribute("video-id");
    	fetch(`/playList/create/${videoId}`, {method:"post"})
    		.then(res => res.text())
    		.then(res => {
    			if(res === "OK"){
    				const miniVideo = makeMiniVideo(video, author);
    				appendMiniVideoToPlayer(miniVideo);
    				registerDelete(miniVideo);
    				const totalCountDiv = document.querySelector(".player_total_count");
    				const totalCount = parseInt(totalCountDiv.innerText);
    				totalCountDiv.innerText = totalCount + 1;
    			} else if(res === "EXIST"){
    				alert("이미 재생목록에 추가되었습니다.")
    			}
    		});
    	
    });
    
    mainVideo.append(cardDiv);
  });
};

const makeVideoBox = (video, author) => {
  let div = ``;
  div += `<a href="/video/${video.id}" class="flex_col">`;
  div += `	<div class="video_wrapper" id="video${video.id}">`;
  div += `  	<video src="${video.url}" class="big_card_img" ></video>`;
  div += `		<div class="playList_save" video-id="${video.id}">`;
  div += `			<span>재생목록에 추가</span>`;
  div += `			<img src="/img/playList.svg" class="small_avatar"/>`;
  div += `		</div>`;
  div += `	</div>`;
  div += `  <div class="flex_row big_card_bottom">`;
  div += `      <img src="${author.avatar}" class="middle_avatar align_base" />`;
  div += `      <div class="texts">`;
  div += `          <span class="title">${video.title}</span>`;
  div += `          <div class="gray_font">`;
  div += `              <div class="author gray_font">${author.username}</div>`;
  div += `              <div>`;
  div += `                  <span class="readCount gray_font">조회수 ${video.readCount}</span>`;
  div += `                  <span class="gray_font">·</span>`;
  div += `                  <span class="createDate gray_font">${video.date}</span>`;
  div += `              </div>`;
  div += `          </div>`;
  div += `      </div>`;
  div += `  </div>`;
  div += `</a>`;
  return div;
};

const studioBtn = document.querySelector(".studio_btn");
studioBtn.addEventListener("click", () => {
  window.location.href = "/studio";
});


const init = async () => {
  const videos = await loadMainVideo();
  appendMainVideo(videos);
  page++;
  
  miniInit();
};

init();