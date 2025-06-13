document.getElementById("login-form").addEventListener("submit", function(e) {
  e.preventDefault();

  const formData = new FormData(e.target);
  const userName = formData.get("username");
  const passWord = formData.get("password");

  fetch("/api/csrf-token", {
    credentials: "include"
  })
    .then(res => res.json())
    .then(csrf => {
      return fetch("/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
          username: userName,
          password: passWord,
          [csrf.parameterName]: csrf.token // 보통 "_csrf": "토큰값"
        }),
        credentials: "include"
      });
    })
    .then(res => {
      if (res.ok) {
        location.href = "/"; // 로그인 성공 시 이동
      } else {
        alert("로그인 실패!");
      }
    });
});