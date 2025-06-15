//URL파라미터
const indexUrlParams = new URLSearchParams(window.location.search);
//URL파라미터에서 page값 가져옴. 없다면 1
let currentPage = parseInt(indexUrlParams.get("idx")) || 1;

fetch(`api/project/check?idx=${currentPage}`)
    .then(ref => ref.json())
    .then(data => {
        if(data.project)
        {
            const project = data.project;

            const title = document.getElementById("title");
            title.value = project.title;

            const indate = document.getElementById("indate");
            indate.value = project.indate;

            const introduce = document.getElementById("introduce");
            introduce.value = project.introduce;

            const content = document.getElementById("content");
            content.innerHTML = project.thumbnail;
            content.innerHTML += project.content;
        }
    })