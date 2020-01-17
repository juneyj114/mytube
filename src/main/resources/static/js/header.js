const search_form = document.querySelector(".search_form");
const search_input = document.querySelector(".search_input");
search_form.addEventListener("submit", async e => {
  e.preventDefault();
  const searchWord = search_input.value;
  console.log(`검색어: ${searchWord}`);
  search_input.value = "";
  encodeURI(searchWord);
  if(searchWord.replace(/\s/g, "") !== ""){
	window.location.href = `/video/search?word=${searchWord}`;  
  };
  });

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