const writeText = document.getElementById("writer");

//패치로 유저정보 뽑아오기
fetch("/api/user/data")
    .then(ref => ref.json())
    .then(data =>{
        if(data.error)
        {

        }
        else if(data.user)
        {
            writeText.value = data.user.writer;
        }
    })

//form에 이벤트 추가하기
document.getElementById("notice-add-form").addEventListener("submit", function(e)
{
    e.preventDefault();

    const formData = new FormData(e.target);

    const title = formData.get("title");
    const content = $('#content').summernote('code');

    const notice = {
        title : title,
        content : content,
    }

    fetch(`/api/notice/add`, {
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
            window.location.href = "/";
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