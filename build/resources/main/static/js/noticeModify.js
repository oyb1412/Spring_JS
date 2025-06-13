const urlParams = new URLSearchParams(window.location.search);
let noticeIdx = urlParams.get("idx");

//기본 정보 삽입
if (noticeIdx) {
  fetch(`/api/notice/check?idx=${noticeIdx}`)
    .then(res => res.json())
    .then(data => {
       const cancel = document.getElementById("cancelSubmit");

       cancel.addEventListener("click", function(e){
            e.preventDefault();
            window.location = `/notice-check-page?idx=${noticeIdx}`;
       })

       const title = document.getElementById("title");
       title.value = data.notice.title;

       const writer = document.getElementById("writer");
       writer.value = data.notice.writer;

       const indate = document.getElementById("indate");
       indate.value = data.notice.indate;

       const viewCount = document.getElementById("viewCount");
       viewCount.value = data.notice.viewCount;

       $('#content').summernote('code', data.notice.content);
    })
}

document.getElementById("notice-add-form").addEventListener("submit", function(e)
{
    e.preventDefault();
    const formData = new FormData(e.target);

    const title = formData.get("title");
    const content = $('#content').summernote('code');

    const notice = {
        idx : noticeIdx,
        title : title,
        content : content,
    }

    fetch("/api/notice/modify",{
        method : "POST",
        headers : {
            "Content-Type" : "application/json",
            [window.csrfHeader] : window.csrfToken
        },
        body : JSON.stringify(notice)
    })
      .then(ref => ref.json())
      .then(data =>{
         if(data.error)
        {
            alert(data.error);
        }
        else
        {
            alert(data.success);
            window.location = `/notice-check-page?idx=${noticeIdx}`;
        }
      })
})

document.addEventListener("DOMContentLoaded", () => {
  $('#content').summernote({
    height: 300,
    toolbar: [
      ['style', ['bold', 'underline', 'clear']],
      ['font', ['fontsize', 'color']],
      ['para', ['ul', 'ol', 'paragraph']],
      ['insert', ['link']],
      ['misc', ['undo', 'redo']]
    ]
  });
});