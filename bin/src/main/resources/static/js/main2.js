// 작업 진행도
// 미니플레이어 끝
// 메인페이지 관련 함수 시작하면 됨.

const miniInit = async () => {
  document.querySelector(".player_list").innerHTML = "";
  await createPlayList();
  await playMiniVideo();
  setExitBtn();
  setPlayCount();
  addPlayBtnToPlayingVideo();
};

const createPlayList = async () => {
  const miniVideos = await loadMiniVideos();
  appendMiniVideosToMiniPlayer(miniVideos);
  initPlayingVideoId(miniVideos);
};

const loadMiniVideos = async () => {
  const res = await fetch("/playList");
  const miniVideos = await res.json();
  return miniVideos;
};

const appendMiniVideosToMiniPlayer = miniVideos => {
  miniVideos.forEach(item => {
    const video = item.video;
    const { author } = item.video;
    const miniVideo = makeMiniVideoDiv(video, author);
    appendMiniVideoToMiniPlayer(miniVideo);
    registerDeleteBtn(miniVideo);
  });
};

const makeMiniVideoDiv = (video, author) => {
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

const appendMiniVideoToMiniPlayer = miniVideo => {
  const list = document.querySelector(".player_list");
  list.append(miniVideo);
  miniVideo.addEventListener("click", e => {
    miniVideoClickEvent(miniVideo);
  });
};

const miniVideoClickEvent = async miniVideo => {
  const setVideoId = setItemToLocal(miniVideo);
  const clickedVideo = await loadClickedVideo(setVideoId);
  changeTitle(clickedVideo);
  setPlayCount();
  addPlayBtnToPlayingVideo();
  changeViewer(clickedVideo);
};

const setItemToLocal = miniVideo => {
  videoId = miniVideo.getAttribute("video-id");
  localStorage.setItem("playingVideoId", videoId);
  return videoId;
};

const loadClickedVideo = async videoId => {
  const res = await fetch(`/video/detail/${videoId}`);
  const video = await res.json();
  return video;
};

const changeTitle = video => {
  const titleDiv = document.querySelector(".player_title");
  titleDiv.innerText = video.title;
};

const setPlayCount = () => {
  const listDetails = document.querySelectorAll(".player_list_detail");
  countTotal(listDetails);
  countCurrent(listDetails);
};

const countTotal = listDetails => {
  const totalCount = listDetails.length;
  document.querySelector(".player_total_count").innerText = totalCount;
};

const countCurrent = listDetails => {
  const playingVideoId = localStorage.getItem("playingVideoId");
  let count = 1;
  listDetails.forEach(detail => {
    if (detail.getAttribute("video-id") !== playingVideoId) {
      count++;
    } else {
      document.querySelector(".player_count").innerText = count;
      return;
    }
  });
};

const addPlayBtnToPlayingVideo = () => {
  const playingVideoId = localStorage.getItem("playingVideoId");
  const checks = document.querySelectorAll(".play_check");
  checks.forEach(check => {
    if (check.getAttribute("video-id") === playingVideoId) {
      check.innerText = "▶";
    } else {
      check.innerText = "";
    }
  });
};

const changeViewer = miniVideo => {
  const viewer = document.querySelector(".player_video");
  viewer.src = miniVideo.url;
  addEndedEventToViewer(viewer);
  viewer.addEventListener("loadeddata", e => {
    e.target.play();
  });
};

const addEndedEventToViewer = viewer => {
  viewer.addEventListener("ended", e => {
    const playingVideoId = localStorage.getItem("playingVideoId");
    const listDetails = document.querySelectorAll(".player_list_detail");
    let count = 0;
    listDetails.forEach(detail => {
      if (detail.getAttribute("video-id") !== playingVideoId) {
        count++;
      } else {
        count++;
        miniVideoClickEvent(listDetails[count]);
      }
    });
  });
};

const registerDeleteBtn = miniVideo => {
  removeBtnToggle(miniVideo);
  addClickEventToRemoveBtn(miniVideo);
};

const removeBtnToggle = miniVideo => {
  const remove = miniVideo.querySelector(".remove_list_detail > img");
  miniVideo.addEventListener("mouseenter", e => {
    remove.parentNode.style.display = "block";
  });
  miniVideo.addEventListener("mouseleave", e => {
    remove.parentNode.style.display = "none";
  });
};

const addClickEventToRemoveBtn = miniVideo => {
  const remove = miniVideo.querySelector(".remove_list_detail > img");
  remove.addEventListener("click", e => {
    if (confirm("해당 영상을 재생 목록에서 제외합니다.")) {
      const videoId = e.target.getAttribute("video-id");
      fetch(`/playList/delete/${videoId}`, { method: "post" })
        .then(res => res.text())
        .then(res => {
          if (res === "OK") {
            miniVideo.remove();
            if (localStorage.getItem("playingVideoId") === videoId) {
              localStorage.removeItem("playingVideoId");
              miniInit();
            }
          } else {
            alert("다시 시도해 주세요.");
          }
        });
    }
  });
};

const initPlayingVideoId = miniVideos => {
  if (localStorage.getItem("playingVideoId") === null) {
    const firstVideoId = miniVideos[0].video.id;
    localStorage.setItem("playingVideoId", firstVideoId);
  }
};

const playMiniVideo = async () => {
  const miniVideo = await loadMiniVideo();
  changeViewer(miniVideo);
};

const loadMiniVideo = async () => {
  const playingVideoId = localStorage.getItem("playingVideoId");
  const res = await fetch(`/video/detail/${playingVideoId}`);
  const miniVideo = await res.json();
  return miniVideo;
};

const setExitBtn = () => {
  toggleExitBtn();
  addClickEventToExitBtn();
};

const toggleExitBtn = () => {
  const viewerContainer = document.querySelector(".player_view");
  viewerContainer.addEventListener("mouseenter", e => {
    document.querySelector(".curtain").style.display = "flex";
  });
  viewerContainer.addEventListener("mouseleave", e => {
    document.querySelector(".curtain").style.display = "none";
  });
};

const addClickEventToExitBtn = () => {
  const exitBtn = document.querySelector(".close_player");
  const player = document.querySelector("#mini_player");
  const viewer = document.querySelector(".player_video");
  exitBtn.addEventListener("click", () => {
    viewer.pause();
    player.style.display = "none";
  });
};

// 여기부터는 메인 관련 함수들입니다.

const loadMainVideo = async () => {
  const fetchData = await fetch(`/video/main?page=${page}`);
  const videos = await fetchData.json();
  return videos;
};

const appendMainVideo = videos => {
  const mainVideo = document.querySelector("#main_video");
  videos.forEach(video => {
    const card = makeCard(video);
    addMouseEventToCard(card);
    setAddingToMiniPlayerBtn(card, video);
    mainVideo.append(card);
  });
};

const makeCard = video => {
  const { author } = video;
  const card = document.createElement("div");
  card.classList.add("big_card", "flex_col");
  card.innerHTML = makeVideoBox(video, author);
  return card;
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

const addMouseEventToCard = card => {
  card.addEventListener("mouseenter", e => {
    e.target.querySelector(".playList_save").style.display = "flex";
  });
  card.addEventListener("mouseleave", e => {
    e.target.querySelector(".playList_save").style.display = "none";
  });
};

const setAddingToMiniPlayerBtn = (card, video) => {
  addMouseEventToAddingToMiniPlayerBtn(card);
  addClickEventToAddingToMiniPlayerBtn(card, video);
};

const addMouseEventToAddingToMiniPlayerBtn = card => {
  playListIcon = card.querySelector(".playList_save");
  playListText = card.querySelector(".playList_save > span");
  playListIcon.addEventListener("mouseenter", e => {
    e.target.childNodes[1].style.display = "block";
  });
  playListIcon.addEventListener("mouseleave", e => {
    e.target.childNodes[1].style.display = "none";
  });
};

const addClickEventToAddingToMiniPlayerBtn = (card, video) => {
  playListIcon = card.querySelector(".playList_save");
  playListIcon.addEventListener("click", async e => {
    e.preventDefault();
    showMiniPlayer();
    const status = await setClickedVideoToPlayList(e);
    if (status === "OK") {
      appendClickedVideoToMiniPlayer(video);
      countUp();
    } else if (status === "EXIST") {
      alert("이미 재생목록에 추가되었습니다.");
    }
  });
};

const showMiniPlayer = () => {
  const player = document.querySelector("#mini_player");
  player.style.display = "grid";
};

const setClickedVideoToPlayList = async e => {
  const videoId = e.target.parentNode.getAttribute("video-id");
  const res = await fetch(`/playList/create/${videoId}`, { method: "post" });
  const status = await res.text();
  return status;
};

const appendClickedVideoToMiniPlayer = video => {
  const { author } = video;
  const miniVideo = makeMiniVideoDiv(video, author);
  appendMiniVideoToMiniPlayer(miniVideo);
  registerDeleteBtn(miniVideo);
};

const countUp = () => {
  const totalCountDiv = document.querySelector(".player_total_count");
  const totalCount = parseInt(totalCountDiv.innerText);
  totalCountDiv.innerText = totalCount + 1;
};

let page = 0;

$(window).scroll(async function() {
  if ($(window).scrollTop() == $(document).height() - $(window).height()) {
    const videos = await loadMainVideo();
    appendMainVideo(videos);
    page++;
  }
});

const init = async () => {
  const videos = await loadMainVideo();
  appendMainVideo(videos);
  page++;

  miniInit();

  const studioBtn = document.querySelector(".studio_btn");
  studioBtn.addEventListener("click", () => {
    window.location.href = "/studio";
  });
};

init();
