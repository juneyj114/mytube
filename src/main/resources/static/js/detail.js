let isLogin = false;
let sending = false;
const login_check = document.querySelector("#login_check");
if(login_check){
	isLogin = true;
}

const sub_btn = document.querySelector(".subscribe_btn");
const author_id = sub_btn.getAttribute("author-id");
const fetchSub = async () => {
      const fetchData = await fetch(`/sub/${author_id}`, { method: "post" });
      const status = await fetchData.text();
      if(status === "SAVE"){
    	  sub_btn.classList.add('subing');
    	  sub_btn.innerText = "구독중"
      } else {
    	  sub_btn.classList.remove('subing');
    	  sub_btn.innerText = "구독"
      }
    };

sub_btn.addEventListener("click", fetchSub);

const likeCountDown = async () => {
	like_img.src = "/img/like.svg";
	like_img.classList.remove("liked");
	like_count.innerText = parseInt(like_count.innerText) - 1;
	const fetchData = await fetch(`/like/${video_id}/true`, {method:"post"});
	const res = await fetchData.text();
}

const likeCountUp = async () => {
	like_img.src = "/img/liked.svg";
	like_img.classList.add("liked");
	like_count.innerText = parseInt(like_count.innerText) + 1;
	const fetchData = await fetch(`/like/${video_id}/true`, {method:"post"});
	const res = await fetchData.text();
}

const unlikeCountDown = async () => {
	unlike_img.src = "/img/unlike.svg";
	unlike_img.classList.remove("unliked");
	unlike_count.innerText = parseInt(unlike_count.innerText) - 1;
	const fetchData = await fetch(`/like/${video_id}/false`, {method:"post"});
	const res = await fetchData.text();
}

const unlikeCountUp = async () => {
	unlike_img.src = "/img/unliked.svg";
	unlike_img.classList.add("unliked");
	unlike_count.innerText = parseInt(unlike_count.innerText) + 1;
	const fetchData = await fetch(`/like/${video_id}/false`, {method:"post"});
	const res = await fetchData.text();
}
const like_btn = document.querySelector(".like_btn");
const unlike_btn = document.querySelector(".unlike_btn");
const like_img = document.querySelector(".like_btn > img");
const like_count = document.querySelector(".like_btn > div");
const unlike_img = document.querySelector(".unlike_btn > img");
const unlike_count = document.querySelector(".unlike_btn > div");
const video_id = like_btn.getAttribute("video-id");


like_btn.addEventListener("click", async () => {
	if(!isLogin){
		return;
	}
	if(!sending){
		if(like_img.classList.contains("liked")){ // 좋아요 하는중인 상태라면
			sending = true;
			await likeCountDown(); // 좋아요 -1
		} else { // 좋아요 안하고있는 상태라면
			if(unlike_img.classList.contains("unliked")){ // 싫어요 하는중인 상태라면 (싫어요만 하고있는 상태)
				sending = true;
				await unlikeCountDown(); // 싫어요 -1
				await likeCountUp(); // 좋아요 +1
			} else { // 싫어요 안하고있는 상태라면 (둘 다 안하고 있는 상태)
				sending = true;
				await likeCountUp(); // 좋아요 +1
			}
		}
		sending = false;
	}
});

unlike_btn.addEventListener("click", async () => {
	if(!isLogin){
		return;
	}
	if(!sending){
		if(unlike_img.classList.contains("unliked")){
			sending = true;
			await unlikeCountDown();
		} else {
			if(like_img.classList.contains("liked")) { // 좋아요 눌러놓은 상태라면 취소하고 싫어요 +1
				sending = true;
				await likeCountDown();
				await unlikeCountUp();
			} else {
				sending = true;
				await unlikeCountUp();
			}
		}
		sending = false;	
	}
	
});

