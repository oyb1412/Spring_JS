//URL파라미터
const indexUrlParams = new URLSearchParams(window.location.search);
//URL파라미터에서 page값 가져옴. 없다면 1
let currentPage = parseInt(indexUrlParams.get("page")) || 1;
let searchType = indexUrlParams.get("searchType") || "";
let keyword = indexUrlParams.get("keyword") || "";

console.log(currentPage);
//패치
//유저가 어드민 or 매니저일때만
//get으로 버튼 id가져와서 .style .display ="inline-block" <> "none"로 변경
fetch("/api/user/adminOrManager")
    .then(ref => ref.json())
    .then(data => {
        const button = document.getElementById("createButton");

        if(data.user == "ADMIN" || data.user == "MANAGER")
        {
            button.style.display = "inline-block";
        }
        else
        {
            button.style.display = "none";
        }
    });



//search-form ID를 지닌 태그에 submt이벤트 추가
document.getElementById("search-form").addEventListener("submit", function(e) {
  // 기본 제출 막기
  e.preventDefault(); 

  const formData = new FormData(e.target);

  //name속성이 있는 태그에서 찾음
  searchType = formData.get("searchType");
  keyword = formData.get("keyword");

  currentPage = 1;
  fetchAndRenderPage(currentPage);
  //컨트롤러에선 @RequestParam("searchType") String searchType 등으로 받음.

  fetch(`/api/notice/list?page=${currentPage}&searchType=${encodeURIComponent(searchType)}&keyword=${encodeURIComponent(keyword)}`)
    .then(res => res.json())
    .then(data => {
      const table = document.getElementById("notice-table-body");
      table.innerHTML = ""; // 초기화

      data.noticeList.forEach(notice => {
        const tr = document.createElement("tr");
        tr.style = "cursor:pointer";
        tr.onclick = () => {
          location.href = `/notice-check-page?idx=${notice.idx}`;
        };

        tr.innerHTML = `
          <td>${notice.idx}</td>
          <td>${notice.title}</td>
          <td>${notice.writer}</td>
          <td>${notice.indate}</td>
          <td>${notice.viewCount}</td>
          <td>${notice.commentCount}</td>
        `;
        table.appendChild(tr);
      });
    });

});

//페이지네이션
let totalPage = 0;
const pagination = document.querySelector(".pagination");

//현재페이지 서버에서 얻어오기
fetch(`/api/notice/list?page=${currentPage}&searchType=${encodeURIComponent(searchType)}&keyword=${encodeURIComponent(keyword)}`)
    .then(ref => ref.json())
    .then(data =>{
        //현재페이지 얻어오기
        currentPage = data.currentPage;
        //토탈페이지 얻어오기
        totalPage = data.totalPage;
        fetchAndRenderPage(currentPage, totalPage);
        renderPagination(currentPage, totalPage);
    });

function renderPagination(currentPage, totalPage) {
  pagination.innerHTML = ""; 
  if (currentPage > 1) {
    const backBtn = document.createElement("button");
    backBtn.classList.add("my-pagination-btn");
    backBtn.textContent = "이전";
    backBtn.onclick = () => fetchAndRenderPage(currentPage - 1); 
    pagination.appendChild(backBtn);
  }

  for (let i = 1; i <= totalPage; i++) {
    const numBtn = document.createElement("button");
    numBtn.classList.add("my-pagination-btn");
    numBtn.textContent = i;
    numBtn.style.fontWeight = i === currentPage ? "bold" : "";
    numBtn.onclick = () => fetchAndRenderPage(i);
    pagination.appendChild(numBtn);
  }

  if (currentPage < totalPage) {
    const nextBtn = document.createElement("button");
    nextBtn.classList.add("my-pagination-btn");
    nextBtn.textContent = "다음";
    nextBtn.onclick = () => fetchAndRenderPage(currentPage + 1);
    pagination.appendChild(nextBtn);
  }
}

function fetchAndRenderPage(page)
{
    fetch(`/api/notice/list?page=${page}&searchType=${encodeURIComponent(searchType)}&keyword=${encodeURIComponent(keyword)}`)
                  .then(ref => ref.json())
                  .then(data => {
                        const table = document.getElementById("notice-table-body");
                        table.innerHTML = ""; // 초기화

                        data.noticeList.forEach(notice => {
                        const tr = document.createElement("tr");
                        tr.style = "cursor:pointer";
                        tr.onclick = () => {
                          location.href = `/notice-check-page?idx=${notice.idx}`;
                        };

                        tr.innerHTML = `
                         <td>${notice.idx}</td>
                         <td>${notice.title}</td>
                         <td>${notice.writer}</td>
                         <td>${notice.indate}</td>
                         <td>${notice.viewCount}</td>
                         <td>${notice.commentCount}</td>
                        `;
        table.appendChild(tr);
      });
                       currentPage = page;
                       totalPage = data.totalPage;
                       renderPagination(currentPage, totalPage);
                       history.pushState(null, "", `?page=${currentPage}`);
                  })
}

