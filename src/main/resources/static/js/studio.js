const inputFile = document.querySelector("#file");
const upload_btn = document.querySelector("#upload");
const upload_popup = document.querySelector(".upload");
const exit = document.querySelector("#exit");
//let preview_video = null;
let uploading = false;
const makeDetailBox = (video) => {
  let div = ``;
  div += `	<div class="video_wrapper">`;
  if(video){
	  div += `<video src="${video.url}" class="preview_video"></video>`;
  } else {
	  div += `		<div class="progress_bar">`;
	  div += `			<div class="bar"></div>`;
	  div += `			<div class="per">0%</div>`;
	  div += `		</div>`;  
  };
  
  div += `	</div>`;
  div += `	<form action="video/uploadDetail" method="POST">`;
  if(video){
	  div += `		<input type="text" name="title" class="input detail_input detail_title" placeholder="Title" maxlength="50" id="upload_title" value="${video.title}"/>`;
	  div += `		<textarea name="content" id="upload_content" class="input detail_input" cols="30" rows="10" placeholder="Content">${video.content? video.content : ""}</textarea>`;
  } else {
	  div += `		<input type="text" name="title" class="input detail_input detail_title" placeholder="Title" maxlength="50" id="upload_title"/>`;
	  div += `		<textarea name="content" id="upload_content" class="input detail_input" cols="30" rows="10" placeholder="Content"></textarea>`;  
  };
  div += `		<div class="detail_btn flex_row">`;
  div += `			<select name="pub" id="upload_pub">`;
  if(!video || video.isPublic){
	  div += `				<option value=true selected>공개</option>`;
	  div += `				<option value=false>비공개</option>`;
  } else {
	  div += `				<option value=true>공개</option>`;
	  div += `				<option value=false selected>비공개</option>`;
  };  
  div += `			</select>`;
  div += `		<button type="button" class="submit">게시</button>`;
  div += `		</div>`;
  div += `	</form>`;
  return div;
};

const appendDetailBox = (video) => {
  const upload_popup = document.querySelector(".upload_popup");
  const upload_file = document.querySelector(".upload_file");
  const div = document.createElement("div");
  div.classList.add("upload_detail");
  if(video){
	  div.innerHTML = makeDetailBox(video);
  } else {
	  div.innerHTML = makeDetailBox();
  }
  upload_file.remove();
  upload_popup.append(div);

  const submitBtn = document.querySelector(".submit");
  submitBtn.addEventListener("click", async () => {
    if (uploading) {
      alert("업로드 중입니다. 업로드 완료 후 다시 시도해 주세요.");
      return;
    }
    const title = document.querySelector("#upload_title").value;
    const content = document.querySelector("#upload_content").value;
    const pub = document.querySelector("#upload_pub").value;
    preview_video = document.querySelector(".preview_video");
    const url = preview_video.src;
    console.log(title, content, pub, url);
    const data = {
      title,
      content,
      pub,
      url
    };
    const fetchData = await fetch("/video/uploadDetail", {
      method: "POST",
      body: JSON.stringify(data),
      headers: {
        "Content-Type": "application/json;charset=UTF-8"
      }
    });
    const result = await fetchData.text();
    console.log(result);
    if (result === "OK") {
      window.location.href = "/studio";
    } else {
      alert("다시 시도해주세요.");
    }
  });
};

upload_btn.addEventListener("click", () => {
  upload_popup.style.display = "block";
});
upload_popup.addEventListener("click", e => {
  if (e.target.className === "upload") {
	if(confirm("영상이 업로드 중일 경우 취소될 수 있습니다. 계속하시겠습니까?")){
		window.location.href = "/studio";
	}
  }
});
exit.addEventListener("click", () => {
	if(confirm("영상이 업로드 중일 경우 취소될 수 있습니다. 계속하시겠습니까?")){
		window.location.href = "/studio";
	}
});
const container = document.querySelector(".upload_file");
container.addEventListener("dragenter", e => {
  e.stopPropagation();
  e.preventDefault();
});
container.addEventListener("dragover", e => {
  e.stopPropagation();
  e.preventDefault();
});
container.addEventListener("drop", async e => {
  e.stopPropagation();
  e.preventDefault();
  const droppedFile = e.dataTransfer.files;
  inputFile.files = droppedFile;

  appendDetailBox();
  transFile(droppedFile);
});
inputFile.addEventListener("change", e => {
  console.log(inputFile.files);

  appendDetailBox();
  transFile(inputFile.files);
});

const transFile = async files => {
  uploading = true;
  const formData = new FormData();
  formData.append("file", files[0]);
  const bar = document.querySelector(".bar");
  const per = document.querySelector(".per");
  const fetchData = await axios.post("/video/upload", formData, {
    onUploadProgress: function(e) {
      const loaded = e.loaded;
      const total = e.total;
      const percent = parseInt((loaded / total) * 100) + "%";
      bar.style.width = percent;
      per.innerText = percent;
    }
  });
  const res = await fetchData.data;
  console.log(res);
  video_wrapper = document.querySelector(".video_wrapper");
  video_wrapper.innerHTML = `<video src="${res}" class="preview_video"></video>`;
  uploading = false;
};

const video_containers = document.querySelectorAll("#video_container");
if (video_containers != null) {
  video_containers.forEach(video_container => {
    video_container.addEventListener("mouseenter", e => {
      parent = e.target.querySelector("#btn_change");
      firstChild = parent.children[0];
      firstChild.classList.add("hide");
      secChild = parent.children[1];
      secChild.classList.remove("hide");
    });

    video_container.addEventListener("mouseleave", e => {
      parent = e.target.querySelector("#btn_change");
      firstChild = parent.children[0];
      firstChild.classList.remove("hide");
      secChild = parent.children[1];
      secChild.classList.add("hide");
    });
  });
  
  writes = document.querySelectorAll(".write");
  writes.forEach(ele => {
	  ele.addEventListener("click", async (e) => {
		  const video_id = ele.getAttribute("video-id");
		  const res = await fetch(`/video/detail/${video_id}`, {method: "GET"});
		  const video = await res.json();
		  upload_popup.style.display = "block";
		  appendDetailBox(video);
		  //////////////////////////////////////////////////////////////////////////////
	  });
  });

  deletes = document.querySelectorAll(".delete");
  deletes.forEach(ele => {
    ele.addEventListener("click", e => {
      video_id = e.target.getAttribute("video-id");
      if (confirm("해당 영상을 삭제하시겠습니까?")) {
        fetch(`/video/delete/${video_id}`, { method: "POST" })
          .then(res => res.text())
          .then(res => {
            if (res === "OK") {
              location.href = "/studio";
            } else {
              alert("영상이 삭제되지 않았습니다. 다시 시도해 주세요.");
            }
          });
      }
    });
  });
}

all_check = document.querySelector("#all_check");
delete_many = document.querySelector(".delete_many");
checks = document.querySelectorAll(".check");
checks.forEach(check => {
  check.addEventListener("click", e => {
    if (e.target.checked) {
      delete_many.style.opacity = 1;
      delete_many.classList.add("cursor");
    } else {
      delete_many.style.opacity = 0;
      delete_many.classList.remove("cursor");
    }
  });
});
all_check.addEventListener("click", e => {
  if (e.target.checked) {
    checks.forEach(ele => {
      ele.checked = true;
    });
    delete_many.style.opacity = 1;
    delete_many.classList.add("cursor");
  } else {
    checks.forEach(ele => {
      ele.checked = false;
    });
    delete_many.style.opacity = 0;
    delete_many.classList.remove("cursor");
  }
});
delete_many.addEventListener("click", e => {
  if (checks.length === 0) {
    alert("삭제할 영상이 없습니다.");
    return;
  }
  if (confirm("해당 영상들을 모두 삭제하시겠습니까?")) {
    checks.forEach(ele => {
      if (ele.checked) {
        fetch(`/video/delete/${ele.value}`, { method: "POST" })
          .then(res => res.text())
          .then(res => {
            if (res !== "OK") {
              alert("오류가 발생했습니다. 다시 시도해 주세요.");
            }
          });
      }
    });
    setTimeout(() => {
      location.href = "/studio";
    }, 1000);
  }
});

const makeVideoCard = (video) => {
	card = document.createElement("div");
	card.classList.add("search_card");
	div = ``;
	div += `	<video src="${video.url}" class="search_video"></video>`;
	div += `	<div class="flex_col" >`;
	div += `		<div class="title">${video.title}</div>`;
	div += `		<div class="content">${video.content}</div>`;
	div += `	</div>`;
	card.innerHTML = div;
	return card;
}
const searchResult = document.querySelector(".search_result");
const appendToResarch = (videos) => {
	
	let i = 0;
	videos.forEach(video => {
		videoCard = makeVideoCard(video);
		searchResult.append(videoCard);
		i += 1;
		if(i >=5){
			return;
		}
	});

};

const searchInput = document.querySelector(".search_input");
searchInput.addEventListener(
  "input",
  _.debounce(e => {
    const search = e.target.value;
    const searchUrl = encodeURI(search);
    if (search !== "") {
      searchResult.classList.remove("hide");
      fetch(`/studio/preview/${searchUrl}`, { method: "get" })
      	.then(res => res.json())
      	.then((videos) => {
      		searchResult.innerHTML="";
      		appendToResarch(videos)});
    } else {
      searchResult.classList.add("hide");
    }
  }, 300)
);
