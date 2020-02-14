const search_form = document.querySelector(".search_form");
const search_input = document.querySelector(".search_input");

search_form.addEventListener("submit", async e => {
  e.preventDefault();
  const searchWord = search_input.value;
  console.log(`검색어: ${searchWord}`);
  search_input.value = "";
  encodeURI(searchWord);
  if(searchWord.replace(/\s/g, "") !== ""){
	const res = await fetch(`/video/search?word=${searchWord}`);
	const videos = await res.json();
	appendVideos(videos);
	history.pushState({data:'a'}, '', '/search');
  };
});

const appendVideos = videos => {
	const mainVideo = document.querySelector("#main_video");
	mainVideo.innerHTML = "";
	videos.forEach(video => {
	  const card = makeCard(video);
	  addMouseEventToCard(card);
	  setAddingToMiniPlayerBtn(card, video);
	  mainVideo.append(card);
	});
};

let spreading = false;

const my_account = document.querySelector("#myAccount");
const icon = document.querySelector(".avatar_wrapper");
icon.addEventListener("click", () => {
	if (!spreading) {
		my_account.style.display = "block";
		spreading = true;
	} else {
		my_account.style.display = "none";
		spreading = false;
	}
});

