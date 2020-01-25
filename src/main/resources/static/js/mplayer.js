const player = document.querySelector("#mini_player");

const more = () => {
	player.classList.toggle("extend");
	if(player.classList.contains("extend")){
		player.querySelector(".more").src = "/img/down.svg"
	} else {
		player.querySelector(".more").src = "/img/up.svg"
	}
}

const details = document.querySelectorAll(".player_list_detail");
details.forEach(detail => {
	const remove = detail.querySelector(".remove_list_detail > img")
	detail.addEventListener("mouseenter", (e) => {
		remove.parentNode.style.display = "block";
	});
	detail.addEventListener("mouseleave", (e) => {
		remove.parentNode.style.display = "none";
	});
	remove.addEventListener("click", (e) => {
		if(confirm("해당 영상을 재생 목록에서 제외합니다.")){
			detail.remove();
			// TODO DB에서 삭제하기
		};
	});
});