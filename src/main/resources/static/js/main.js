let page = 0;
let isMain = true;

$(window).scroll(async function() {
  if (
    isMain &&
    $(window).scrollTop() >= $(doument).height() - $(window).height() - 100
  ) {
    const videos = await loadMainVideo();
    appendMainVideo(videos);
    page++;
  }
});

const loadMainVideo = async () => {
  const fetchData = await fetch(`/video/main?page=${page}`);
  const videos = await fetchData.json();
  return videos;
};

const appendMainVideo = videos => {

  const mainVideo = document.querySelector("#main_video");
  videos.forEach(video => {
    const { author } = video;
    const cardDiv = document.createElement("div");
    cardDiv.classList.add("big_card", "flex_col");
    cardDiv.innerHTML = makeVideoBox(video, author);
    mainVideo.append(cardDiv);
  });
};

const makeVideoBox = (video, author) => {
  let div = ``;
  div += `<a href="/video/${video.id}" class="flex_col">`;
  div += `	<div class="video_wrapper" id="video${video.id}">`;
  div += `  	<video src="${video.url}" class="big_card_img" />`;
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
};

init();