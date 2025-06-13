fetch("/api/user/status")
    .then(res => {
    return res.json();
  })
  .then(data => {
    const loginBox = document.getElementById("login-box");

    if (data.isAuthenticated) {
      loginBox.innerHTML = `
        <p style="text-align:center; font-size: 15px; margin-bottom: 10px;">${data.username}님</p>
        <a href="/logout" class="sidebar-btn">로그아웃</a>
        <a href="/myPage.html" class="sidebar-btn">마이페이지</a>
        ${data.admin ? `<a href="/admin.html" class="sidebar-btn">유저 관리</a>` : ""}
      `;
    } else {
      loginBox.innerHTML = `
        <a href="/login.html" class="sidebar-btn">로그인</a>
        <a href="/register.html" class="sidebar-btn">회원가입</a>
        <a href="/findPassword.html" class="sidebar-btn">비밀번호 찾기</a>
      `;
    }
  });

const sideBarUrlParams = new URLSearchParams(window.location.search);
const result = sideBarUrlParams.get("result");
if (result) {
  alert(result);
}