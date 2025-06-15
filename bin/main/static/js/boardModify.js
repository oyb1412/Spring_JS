//URL파라미터
const indexUrlParams = new URLSearchParams(window.location.search);
//URL파라미터에서 page값 가져옴. 없다면 1
let currentPage = parseInt(indexUrlParams.get("idx")) || 1;

//글 정보 가져와서 출력
fetch(`/api/board/check?idx=${currentPage}`)
    .then(ref => ref.json())
    .then(data => {
        console.log(data);
        if(data.board)
        {
            console.log(data.board);
            const title = document.getElementById("title");
            title.value = data.board.title;

            const writer = document.getElementById("writer");
            writer.value = data.board.writer;

            const indate = document.getElementById("indate");
            indate.value = data.board.indate;

            $('#content').summernote('code', data.board.content);

            const viewCount = document.getElementById("viewCount");
            viewCount.value = data.board.viewCount;

            //글 수정 이벤트 등록
            document.getElementById("modify-form").addEventListener("submit", function(e){
                e.preventDefault();

                const board = {
                    idx : currentPage,
                    title : title.value,
                    content : $('#content').summernote('code'),
                    writer : writer.value,
                    indate : indate.value,
                    viewCount : viewCount.value
                }
                
                fetch("api/board/modify", {
                    method : "POST",
                    headers : {
                        "Content-Type" : "application/json",
                        [csrfHeader] : csrfToken
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
                            location.href = "board-list-page";
                        }
                    })
            });
        }
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

