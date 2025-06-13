//URL파라미터
const indexUrlParams = new URLSearchParams(window.location.search);
//URL파라미터에서 page값 가져옴. 없다면 1
let currentPage = parseInt(indexUrlParams.get("idx")) || 1;

let userIdx;
let boardUserName;
let boardWriter;
let boardTitle;
//기본 값들 채워넣기
fetch(`/api/board/check?idx=${currentPage}`)
    .then(ref => ref.json())
    .then(data => {
        if(data.error)
        {
            alert(data.error);
        }
        else
        {
            boardUserName = data.board.memID;

            const title = document.getElementById("title");
            title.value = data.board.title;
            boardTitle = data.board.title;

            const content = document.getElementById("content");
            content.innerHTML = data.board.content;

            const writer = document.getElementById("writer");
            writer.value = data.board.writer;
            boardWriter = data.board.writer;

            const indate = document.getElementById("indate");
            indate.value = data.board.indate;

            const viewCount = document.getElementById("viewCount");
            viewCount.value = data.board.viewCount;

            const upCount = document.getElementById("vote-up-count");
            upCount.textContent = data.board.upCount;

            const downCount = document.getElementById("vote-down-count");
            downCount.textContent = data.board.downCount;

            //코멘트 리스트 가져오기
            const commentList = data.commentList;

            const commentSection = document.getElementById("comment-section");
            //코멘트 리스트가 있을때만 코멘트 섹션 렌더링
            if(commentList.length > 0)
            {
                commentSection.style.display = "inline-block";

                //코멘트 리스트 반복하며 렌더링
                commentList.forEach(comment => {
                    //유저가 코멘트 작성자인지 확인용
                    const isUser = (data.user && comment.writer === data.user.writer);

                    let html = `
             <div class="comment-box" style="position: relative;">
                <strong class="comment-writer">${comment.writer}</strong>
                <span class="comment-date">(${comment.indate})</span>
            `;

            if (isUser) {
             html += `
               <form class="comment-delete-button"  method="post" action="javascript:void(0)"
                      style="position: absolute; top: 0; right: 0;">
                 <input type="hidden" name="commentIdx" value="${comment.idx}"/>
                 <input type="hidden" name="parentIdx" value="${comment.parentIdx}" />
                 <button type="submit" style="
                    background: none;
                    border: none;
                    color: red;
                    font-size: 30px;
                    cursor: pointer;
                    " onclick="return confirm('댓글을 삭제하시겠습니까?')">🗑</button>
               </form>
              `;
            }

            html += `
            <p>${comment.content}</p>
            </div>
            `;

            commentSection.innerHTML += html;
                });

            //모든 삭제 버튼 
            const forms = document.getElementsByClassName("comment-delete-button");

            //모든 삭제 버튼에 삭제 이벤트 할당
            for (let i = 0; i < forms.length; i++) {
                const form = forms[i];
  form.addEventListener("submit", function (e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const commentIdx = formData.get("commentIdx");
    console.log(commentIdx);

    fetch(`/api/comment/delete?idx=${commentIdx}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        [window.csrfHeader]: window.csrfToken
      }
    })
    .then(res => res.json())
    .then(data => {
      if (data.error) alert(data.error);
      else {
        alert(data.success);
        location.reload();
      }
    });
  });
            }
            }
            else
            {
                commentSection.style.display = "none";
            }
        }
    })

//너무 길어서 나눠서
//로그인 상태 체크 후 댓글 작성 가능/불가능 
fetch("/api/user/status")
    .then(ref => ref.json())
    .then(data => {
        const loginComment = document.getElementById("login-comment");
        const logoutComment = document.getElementById("logout-comment");
        const createCommentForm = document.getElementById("create-comment-button");

        if(data.isAuthenticated)
        {
            loginComment.style.display = "inline-block";
            logoutComment.style.display = "none";
            createCommentForm.style.display = "inline-block";

            //코멘트 작성 이벤트 할당
            document.getElementById("create-comment-form").addEventListener("submit", function(e){
                e.preventDefault();

                const content = loginComment.value;

                const comment = {
      parentIdx: currentPage,
      content: content,
      boardType: "BOARD",
    };


    fetch("/api/comment/create",
      {
        method : "POST",
        headers : {
          "Content-Type" : "application/json",
          [window.csrfHeader] : window.csrfToken
        },
        body : JSON.stringify(comment)
      }
    )
      .then(ref => ref.json())
      .then(data => 
      {
          if(data.error)
          {
              alert(data.error);
          }
          else
          {
              alert(data.success);
              location.reload();
          }
      }
    )
            })
        }
        else
        {
            loginComment.style.display = "none";
            logoutComment.style.display = "inline-block";
            createCommentForm.style.display = "none";
        }
    })


//로그인 정보 불러오기
//메니저/어드민/글 작성자만 수정/삭제 버튼 렌더링
//로그인 한 상태에서만 신고 버튼 렌더링
fetch("/api/user")
    .then(ref => ref.json())
    .then(data =>
    {
        const reportBtn = document.getElementById("report-button");
        if(data.user)
        {
            userIdx = data.user.idx;
            reportBtn.style.display = "inline-block";

            let canModify = false;

            //로그인한 유저네임 - board.memID 체크
            if(data.user.username == boardUserName)
            {
                canModify = true;
            }
            //로그인한 유저 권한 체크
            fetch("api/user/status")
                .then(ref => ref.json())
                .then(data => {
                    if(data.user == "MANAGER" || data.user == "ADMIN")
                    {
                        canModify = true;
                    }
                })

            const buttonContainer = document.getElementById("buttonContainer");
            
            //수정 가능한 대상이면 버튼 렌더링
            if(canModify)
            {
                buttonContainer.display = "inline-block";
            }
            //수정 불가능한 대상이면 버튼 비 렌더링
            else
            {
                buttonContainer.display = "none";
            }
        }
        else
        {
            reportBtn.style.display = "none";
        }
    }
)


//수정 버튼 이벤트 할당
document.getElementById("modify-form").addEventListener("submit", function(e){
    e.preventDefault();

    location.href = `/board-modify-page&idx=${currentPage}`;
})

//삭제 버튼 이벤트 할당
document.getElementById("delete-form").addEventListener("submit", function(e){
    e.preventDefault();

    fetch(`/api/board/delete&idx=${currentPage}`,{
        method : "DELETE",
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
            else
            {
                alert(data.success);
                location.href = "/";
            }
        })
})


//신고 버튼 이벤트 할당
document.getElementById("report-form").addEventListener("submit", function(e){
    e.preventDefault();

    const url = '/board-report-page?boardIdx=' + encodeURIComponent(currentPage)
              + '&reportedUserIdx=' + encodeURIComponent(userIdx)
              + '&writer=' + encodeURIComponent(boardWriter)
              + '&title=' + encodeURIComponent(boardTitle);

    window.open(url, 'reportWindow', 'width=500,height=400,resizable=no');
})




//좋아요, 싫어요 버튼 이벤트 할당
//이벤트 후 새로고침 필요함
document.getElementById("vote-up-form").addEventListener("submit", function(e){
    e.preventDefault();

    fetch(`/api/board/vote&idx=${currentPage}&voteType=up`,{
        method : "POST",
        headers : {
            [window.csrfHeader] : window.csrfToken
        }
    })
        .then(ref => ref.json())
        .then(data =>{
            if(data.error)
            {
                alert(data.error);
            }
            else if(data.success)
            {
                alert(data.success);
                location.reload();
            }
        })
})

document.getElementById("vote-down-form").addEventListener("submit", function(e){
    e.preventDefault();

    fetch(`/api/board/vote&idx=${currentPage}&voteType=down`,{
        method : "POST",
        headers : {
            [window.csrfHeader] : window.csrfToken
        }
    })
        .then(ref => ref.json())
        .then(data =>{
            if(data.error)
            {
                alert(data.error);
            }
            else if(data.success)
            {
                alert(data.success);
                location.reload();
            }
        })
})
