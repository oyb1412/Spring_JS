// header
fetch("/views/common/header.html")
  .then(res => res.text())
  .then(html => {
    document.getElementById("header-area").innerHTML = html;
    const script = document.createElement("script");
    script.src = "/js/header.js";
    document.body.appendChild(script);
  });

// sidebar
fetch("/views/common/sidebar.html")
  .then(res => res.text())
  .then(html => {
    document.getElementById("sidebar-area").innerHTML = html;
    const script = document.createElement("script");
    script.src = "/js/sidebar.js";
    document.body.appendChild(script);
  });

// footer
fetch("/views/common/footer.html")
  .then(res => res.text())
  .then(html => {
    document.getElementById("footer-area").innerHTML = html;
    // 필요하면 footer.js도 동적으로 불러올 수 있음
  });