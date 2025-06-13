let writer = "";

fetch("api/user")
    .then(ref => ref.json())
    .then(data => {
        writer = data.user.writer;
        const writerForm = document.getElementById("writer");
        writerForm.value = writer;
    })



document.getElementById("create-form").addEventListener("submit", function(e){
    e.preventDefault();

    const formData = new FormData(e.target);

    const title = formData.get("title");
    const content = formData.get("content");

    const board = {
        title : title,
        content : content,
        writer : writer
    }
    fetch("/api/board/add", {
        method : "POST",
        headers : {
            "Content-Type" : "application/json",
            [window.csrfHeader] : window.csrfToken
        },
        body : JSON.stringify(board)
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
                location.href = "board-check-page?idx=1"
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