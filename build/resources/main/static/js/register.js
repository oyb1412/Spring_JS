document.getElementById("register-form").addEventListener("submit", function(e){
    e.preventDefault();

    const formData = new FormData(e.target);

    const userName = formData.get("username");
    const passWord = formData.get("password");
    const writer = formData.get("writer");

    fetch("/api/csrf-token", {
    credentials: "include"
  })
    .then(res => res.json())
    .then(csrf => {
      return fetch("/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
          username: userName,
          password: passWord,
          writer : writer,
          [csrf.parameterName]: csrf.token 
        }),
        credentials: "include"
      });
    })
    .then(res => {
      if (res.ok) {
        alert("회원가입에 성공했습니다");
        location.href = "/";
      } else if(res.status == 400) {
        alert("아이디를 작성해주세요");
      }
      else if(res.status == 404)
      {
        alert("비밀번호를 작성해주세요");
      }
      else if(res.status == 409)
      {
        alert("이미 존재하는 아이디입니다");
      }
      else if(res.status == 500)
      {
        alert("알 수 없는 오류로 회원가입에 실패했습니다");
      }
      else if(res.status == 403)
      {
        alert("닉네임을 입력해주세요");
      }
    });
})