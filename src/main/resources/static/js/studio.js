const inputFile = document.querySelector("#file");
const upload_btn = document.querySelector("#upload");
const upload_popup = document.querySelector(".upload");
const exit = document.querySelector("#exit");
//let preview_video = null;
let uploading = false;
const makeDetailBox = () => {
  let div = ``;
  div += `	<div class="video_wrapper">`;
//  div += `		<video src="#" class="preview_video"></video>`;
  div += `		<div class="progress_bar">`;
  div += `			<div class="bar"></div>`;
  div += `			<div class="per">0%</div>`;
  div += `		</div>`;
  div += `	</div>`;
  div += `	<form action="video/uploadDetail" method="POST">`;
  div += `		<input type="text" name="title" class="input detail_input detail_title" placeholder="Title" maxlength="50" id="upload_title"/>`;
  div += `		<textarea name="content" id="upload_content" class="input detail_input" cols="30" rows="10" placeholder="Content"></textarea>`;
  div += `		<div class="detail_btn flex_row">`;
  div += `			<select name="pub" id="upload_pub">`;
  div += `				<option value=true selected>공개</option>`;
  div += `				<option value=false>비공개</option>`;
  div += `			</select>`;
  div += `		<button type="button" class="submit">게시</button>`;
  div += `		</div>`;
  div += `	</form>`;
  return div;
};

const appendDetailBox = () => {
  const upload_popup = document.querySelector(".upload_popup");
  const upload_file = document.querySelector(".upload_file");
  const div = document.createElement("div");
  div.classList.add("upload_detail");
  div.innerHTML = makeDetailBox();
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
    upload_popup.style.display = "none";
  }
});
exit.addEventListener("click", () => {
  upload_popup.style.display = "none";
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

//axios 아닌 fetch 사용 (progressbar X)
//const transFile = async files => {
//  uploading = true;
//  const formData = new FormData();
//  formData.append("file", files[0]);
//  const fetchData = await fetch("/video/upload", {
//    method: "POST",
//    body: formData
//  });
//  const res = await fetchData.text();
//  console.log(res);
//  preview_video = document.querySelector(".preview_video");
//  preview_video.src = res;
//  uploading = false;
//};

const transFile = async files => {
	  uploading = true;
	  const formData = new FormData();
	  formData.append("file", files[0]);
	  const bar = document.querySelector(".bar");
	  const per = document.querySelector(".per");
	  const fetchData = await axios.post("/video/upload", formData, {
		  onUploadProgress: function(e){
			  				const loaded = e.loaded;
			  				const total = e.total;
			  				const percent = parseInt(loaded / total * 100)+"%";
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
