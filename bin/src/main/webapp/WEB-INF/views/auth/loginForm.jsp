<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>Document</title>
    <link rel="stylesheet" href="/css/login.css" />
  </head>
  <body>
    <div class="wrapper">
      <div class="login_text center">
        로그인
      </div>
      <div class="input_container">
        <input type="email" name="" class="login_input" />
        <div class="placeholder">이메일</div>
        <div><a href="#">이메일을 잊으셨나요?</a></div>
      </div>
      <div class="input_container">
        <input type="password" name="" class="login_input" />
        <div class="placeholder">비밀번호</div>
        <div><a href="#">비밀번호를 잊으셨나요?</a></div>
      </div>
      <div class="bottom_btns">
        <div><a href="#">계정만들기</a></div>
        <button class="login_btn">로그인</button>
      </div>
      <div class="hr"></div>
      <img
        src="/img/google.png"
        class="login_img center"
        data-social="google"
      />
      <img
        src="/img/facebook.png"
        class="login_img center"
        data-social="facebook"
      />
    </div>
    <script>
      const social = document.getElementsByClassName("login_img");
      for (var i = 0; i < social.length; ++i) {
        social[i].addEventListener(
          "click",
          function() {
            const socialType = this.getAttribute("data-social");
            location.href = "/oauth2/authorization/" + socialType;
            console.log("click!!!");
          },
          false
        );
      }
      const inputEle = document.querySelectorAll(".input_container input");
      inputEle.forEach(item => {
        item.addEventListener("focus", () => {
          item.classList.add("focus");
        });
        item.addEventListener("blur", () => {
          if (item.value === "") {
            item.classList.remove("focus");
          }
        });
      });
    </script>
  </body>
</html>
