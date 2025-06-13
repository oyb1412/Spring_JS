const urlParams = new URLSearchParams(window.location.search);
let noticeCheckIdx = urlParams.get("idx");

//idx값 채우기
if(noticeCheckIdx)
{
  document.querySelectorAll('input[name="idx"]').forEach(e1 =>{
    e1.value = noticeCheckIdx;
  })
}

//공지사항 글 
if (noticeCheckIdx) {
  fetch(`/api/notice/check?idx=${noticeCheckIdx}`)
    .then(res => res.json())
    .then(data => {
       const title = document.getElementById("title");
       title.value = data.notice.title;
       
       const content = document.getElementById("content");
       content.innerHTML = data.notice.content;

       const writer = document.getElementById("writer");
       writer.value = data.notice.writer;

       const indate = document.getElementById("indate");
       indate.value = data.notice.indate;

       const viewCount = document.getElementById("view-count");
       viewCount.value = data.notice.viewCount;

       //좋아요 및 싫어요 카운트
       const upCount = document.getElementById("vote-up-count");
       upCount.textContent = data.notice.upCount;

       const DownCount = document.getElementById("vote-down-count");
       DownCount.textContent = data.notice.downCount;

       //수정 및 삭제 버튼 표시
       const buttonContainer = document.getElementById("buttonContainer");
       if(data.user && (data.user.role == "MANAGER" || data.user.role == "ADMIN" ||
        data.user.username == data.notice.memID))
       {
          buttonContainer.style.display = "inline-block";
       }
       else
       {
          buttonContainer.style.display = "none";
       }

       //로그인 상태 확인 후 댓글 작성 가능
       const loginTextarea = document.getElementById("login-comment-textarea");
       const logoutTextarea = document.getElementById("logout-comment-textarea");
       const commentButton = document.getElementById("comment-button");
       if(data.auth != null && data.auth == true)
       {
          loginTextarea.style.display = "inline-block";
          logoutTextarea.style.display = "none";
          commentButton.style.display = "inline-block"
       }
       else
       {
          logoutTextarea.style.display = "inline-block";
          loginTextarea.style.display = "none";
          commentButton.style.display = "none";
       }

       //코멘트 정보 받아와서 foreach 돌리기
       const commentSection = document.getElementById("comment-section");

       if(data.commentList.length > 0)
       {
          commentSection.innerHTML += `<h3 class="comment-title">댓글</h3>`;
          commentSection.style.display = "inline-block";
       }
       else
       {
          commentSection.style.display = "none";
       }

       data.commentList.forEach(comment =>{
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
                    cursor: pointer;">🗑</button>
               </form>
              `;
            }

            html += `
            <p>${comment.content}</p>
            </div>
            `;

            commentSection.innerHTML += html;

        })

        const forms = document.getElementsByClassName("comment-delete-button");

  //코멘트 삭제
for (let i = 0; i < forms.length; i++) {
  const form = forms[i];
  console.log("반복문 시작");
  form.addEventListener("submit", function (e) {
    e.preventDefault();
    console.log("이벤트 발견");
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
  console.log("폼 바인딩 완료", form);
}

    });
} else {
  alert("잘못된 접근입니다");
  location.href = "/";
}

//좋아요 클릭
document.getElementById("vote-up-form").addEventListener("submit", function(e){
    e.preventDefault();
    console.log("noticeCheckIdx : " + noticeCheckIdx);

    fetch(`/api/vote`,
      {
        method : "POST",
        headers : {
          "Content-Type": "application/x-www-form-urlencoded",
          [window.csrfHeader] : window.csrfToken
        },
        body : `idx=${noticeCheckIdx}&voteType=up`
      }
    )
      .then(ref => ref.json())
      .then(data => 
      {
          console.log(data);
          //코멘트 up 실패. 얼럿 출력
          if(data.error)
          {
              alert(data.error);
          }
          //코멘트 up 성공
          else
          {
              alert(data.success);
              //성공했으니 바로 ui 갱신
              const upCount = document.getElementById("vote-up-count");
              upCount.textContent = Number(upCount.textContent) + 1;
          }
      }
    )
    . catch(err => {
      console.error("fetch에러 " + err);
    })
}
)

//싫어요 클릭
document.getElementById("vote-down-form").addEventListener("submit", function(e){
    e.preventDefault();

    fetch(`/api/vote?idx=${noticeCheckIdx}&voteType=down`,
      {
        method : "POST",
        headers : {
          [window.csrfHeader] : window.csrfToken
        }
      }
    )
      .then(ref => ref.json())
      .then(data => 
      {
          //코멘트 up 실패. 얼럿 출력
          if(data.error)
          {
              alert(data.error);
          }
          //코멘트 up 성공
          else
          {
              alert(data.success);
              //성공했으니 바로 ui 갱신
              const upCount = document.getElementById("vote-down-count");
              upCount.textContent = Number(upCount.textContent) - 1;
          }
      }
    )
}
)

//코멘트 작성
document.getElementById("comment-submit").addEventListener("submit", function(e)
{
    e.preventDefault();
   
    const content = document.getElementById("login-comment-textarea").value;
    const comment = {
      parentIdx: noticeCheckIdx,
      content: content,
      boardType: "NOTICE",
    };

    console.log(comment.parentIdx);
    console.log(comment.content);
    console.log(comment.boardType);

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

//수정 페이지 이동
document.getElementById("modify-button").addEventListener("submit", function(e){
    e.preventDefault();

    location.href = `/notice-modify-page?idx=${noticeCheckIdx}`;
})

//글 삭제
document.getElementById("delete-button").addEventListener("submit", function(e)
{
    e.preventDefault();
   

    fetch(`/api/notice/delete?idx=${noticeCheckIdx}`,
      {
        method : "DELETE",
        headers : {
          [window.csrfHeader] : window.csrfToken
        }
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

