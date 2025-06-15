//URL파라미터
const indexUrlParams = new URLSearchParams(window.location.search);
//URL파라미터에서 page값 가져옴. 없다면 1
let currentPage = parseInt(indexUrlParams.get("idx")) || 1;

fetch(`/api/board/report/data?idx=${currentPage}`)
    .then(ref => ref.json())
    .then(data =>{
        if(data.user && data.board)
        {
            const title = document.getElementById("title");
            title.textContent = data.board.title;

            const writer = document.getElementById("writer");
            writer.textContent = data.board.writer;

            const repoter = document.getElementById("repoter");
            repoter.textContent = data.user.username;
        }
    })

    let csrfHeader;
    let csrfToken;

     //서버에 Get형식으로 json타입을 요청.
  fetch("/api/csrf-token", {
    method : "Get",
    headers : {
      "Content-Type" : "application/json"
    }
  })
  .then(res => res.json())
  .then(data => {
      csrfHeader = data.headerName;
      csrfToken = data.token;
  });

document.getElementById("report-form").addEventListener("submit", function(e){
    e.preventDefault();

    const reportType = document.querySelector('input[name="reportType"]:checked').value;

    fetch(`/api/board/report?boardIdx=${currentPage}&reportType=${reportType}`,{
        method : "POST",
        headers : {
            [csrfHeader] : csrfToken
        }
    })
    .then(ref => ref.json())
    .then(data => {
        if(data.error)
        {
            alert(data.error);
        }
        else if(data.success)
        {
            alert(data.success);
            window.close();
        }
    })
})