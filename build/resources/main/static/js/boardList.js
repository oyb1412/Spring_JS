//URL파라미터
const indexUrlParams = new URLSearchParams(window.location.search);
//URL파라미터에서 page값 가져옴. 없다면 1
let currentPage = parseInt(indexUrlParams.get("page")) || 1;
let searchType = indexUrlParams.get("searchType") || "";
let keyword = indexUrlParams.get("keyword") || "";
let sortField = indexUrlParams.get("sortField") || "";
let sortOrder = indexUrlParams.get("sortOrder") || "desc";

let totalBoardPage;

//유저 정보 취득(작성 버튼 출력)
fetch("/api/user/status")
    .then(ref => ref.json())
    .then(data => {
        const createButton = document.getElementById("create-button");
        if(data.isAuthenticated)
        {
            createButton.style.display = "inline-block";
        }
        else
        {
            createButton.style.display = "none";
        }
    })

const boardSection = document.getElementById("board-section");

//보드 리스트 출력
fetch(`/api/board/list?page=${currentPage}&searchType=${searchType}&keyword=${keyword}&sortField=${sortField}&sortOrder=${sortOrder}`)
    .then(ref => ref.json())
    .then(data => {
        const boardList = data.boardList;
        boardSection.innerHTML = "";
        boardList.forEach(board => {
            RenderingList(board);
        });
    })


//검색 기능
const form = document.getElementById("search-form");

form.addEventListener("submit", function(e){
    e.preventDefault();

    //현재 선택된 옵션 뽑아오기
    const select = form.querySelector("select");
    searchType = select.value;
    keyword = form.querySelector("#search-box").value;

    //검색 fetch를 보내고, 그 조건에 맞게 한번 더 렌더링 해야함
    fetch(`/api/board/list?page=${currentPage}&searchType=${searchType}&keyword=${keyword}&sortField=${sortField}&sortOrder=${sortOrder}`)
    .then(ref => ref.json())
    .then(data => {
        const boardList = data.boardList;
        boardSection.innerHTML = "";
        boardList.forEach(board => {
            RenderingList(board);
        });
    })

})

//정렬 기능(기본적으로 내림차순)
//버튼을 누를때마다 모든 버튼 textContent초기화 후, 재설정 해야함
const sortSection = document.getElementById("sort-options");
const sortButtons = sortSection.querySelectorAll(".sort-btn");

//조회수 정렬
document.getElementById("view-sort-form").addEventListener("submit", function(e){
    e.preventDefault();

    //일단 모든 버튼 text 초기화
    initSortButton(sortButtons);

    //현재 sortOrder를 기반으로 반대로 바꾸고, text도 교체 필요
    sortOrder = sortOrder == "desc" ? "asc" : "desc";
    sortField = "view";
    sortButtons[0].textContent = sortOrder == "desc" ? "조회수 ▲" : "조회수 ▼";

    //fetch 송신
    fetch(`/api/board/list?page=${currentPage}&searchType=${searchType}&keyword=${keyword}&sortField=${sortField}&sortOrder=${sortOrder}`)
    .then(ref => ref.json())
    .then(data => {
        const boardList = data.boardList;
        boardSection.innerHTML = "";
        boardList.forEach(board => {
            RenderingList(board);
        });
    })
})

//코멘트수 정렬
document.getElementById("comment-sort-form").addEventListener("submit", function(e){
    e.preventDefault();

    //일단 모든 버튼 text 초기화
    initSortButton(sortButtons);
    //현재 sortOrder를 기반으로 반대로 바꾸고, text도 교체 필요
    sortOrder = sortOrder == "desc" ? "asc" : "desc";
    sortField = "comment";
    sortButtons[1].textContent = sortOrder == "desc" ? "코멘트 ▲" : "코멘트 ▼";

    //fetch 송신
    fetch(`/api/board/list?page=${currentPage}&searchType=${searchType}&keyword=${keyword}&sortField=${sortField}&sortOrder=${sortOrder}`)
    .then(ref => ref.json())
    .then(data => {
        const boardList = data.boardList;
        boardSection.innerHTML = "";
        boardList.forEach(board => {
            RenderingList(board);
        });
    })
})

//작성일 정렬
document.getElementById("date-sort-form").addEventListener("submit", function(e){
    e.preventDefault();

    //일단 모든 버튼 text 초기화
    initSortButton(sortButtons);
    //현재 sortOrder를 기반으로 반대로 바꾸고, text도 교체 필요
    sortOrder = sortOrder == "desc" ? "asc" : "desc";
    sortField = "date";
    sortButtons[2].textContent = sortOrder == "desc" ? "작성일 ▲" : "작성일 ▼";

    //fetch 송신
    fetch(`/api/board/list?page=${currentPage}&searchType=${searchType}&keyword=${keyword}&sortField=${sortField}&sortOrder=${sortOrder}`)
    .then(ref => ref.json())
    .then(data => {
        const boardList = data.boardList;
        boardSection.innerHTML = "";
        boardList.forEach(board => {
            RenderingList(board);
        });
    })
})

//페이지네이션
//이전, 1 2 3.., 다음 버튼 렌더링
//각 버튼에 RenderingList 이벤트 추가해야함
const paginationSection = document.querySelector(".pagination");
paginationSection.innerHTML = "";

Pagination();

function Pagination()
{
    paginationSection.innerHTML = "";
    //이전 버튼 표기
if(currentPage > 1)
{
    const button = document.createElement("button");
    button.style = "cursor:pointer"
    button.textContent = "이전";
    button.onclick = () => {
        currentPage--;
        fetch(`/api/board/list?page=${currentPage}&searchType=${searchType}&keyword=${keyword}&sortField=${sortField}&sortOrder=${sortOrder}`)
            .then(ref => ref.json())
            .then(data => {
             const boardList = data.boardList;
             boardSection.innerHTML = "";
             boardList.forEach(board => {
             RenderingList(board);

        });
        Pagination();
    })
    }
    
    paginationSection.appendChild(button);
}

//1 2 3.. , 다음 버튼 표시
//totalBoardPage 받아와야함.
fetch(`/api/board/list?idx=${currentPage}&searchType=${searchType}&keyword=${keyword}&sortField=${sortField}&sortOrder=${sortOrder}`)
            .then(ref => ref.json())
            .then(data => {
                const totalBoardPage = data.totalBoardPage;

                for(let i = 0; i < totalBoardPage; i++)
                {
                    const button = document.createElement("button");
                    button.style = "cursor:pointer"
                    button.textContent = i + 1;
                    if (i + 1 === currentPage) {
                        button.style.fontWeight = "bold";
                    }
                    button.onclick = () => {
                    currentPage = i + 1;
                    fetch(`/api/board/list?page=${currentPage}&searchType=${searchType}&keyword=${keyword}&sortField=${sortField}&sortOrder=${sortOrder}`)
                       .then(ref => ref.json())
                       .then(data => {
                       const boardList = data.boardList;
                       boardSection.innerHTML = "";
                       boardList.forEach(board => {
                       RenderingList(board);

        });
        Pagination();

    })
    }
    paginationSection.appendChild(button);
   }
    if(currentPage < totalBoardPage)
    {
    const button = document.createElement("button");
    button.style = "cursor:pointer"
    button.textContent = "다음";
    button.onclick = () => {
        currentPage++;
        fetch(`/api/board/list?page=${currentPage}&searchType=${searchType}&keyword=${keyword}&sortField=${sortField}&sortOrder=${sortOrder}`)
            .then(ref => ref.json())
            .then(data => {
             const boardList = data.boardList;

             boardSection.innerHTML = "";
             boardList.forEach(board => {
             RenderingList(board);

        });
        Pagination();

    })
    }
    
    paginationSection.appendChild(button);
}
});
}

//모든 버튼 text 초기화
function initSortButton(buttons)
{
    buttons[0].textContent = "조회수 ▲";
    buttons[1].textContent = "코멘트 ▲";
    buttons[2].textContent = "작성일 ▲";
}

//글 리스트 렌더링
function RenderingList(board)
{
    const tr = document.createElement("tr");
            tr.style ="cursor:pointer";
            tr.innerHTML = `
                <td>${board.idx}</td>
                <td>${board.title}</td>
                <td>${board.writer}</td>
                <td>${board.indate}</td>
                <td>${board.viewCount}</td>
                <td>${board.commentCount}</td>
            `;

            tr.onclick = () => {
                location.href =`/board-check-page?idx=${board.idx}`;
            }

            boardSection.appendChild(tr);
            //렌더링 후 url 변경해야 함.
            history.pushState(null, "", `board-list-page?idx=${currentPage}&searchType=${searchType}&keyword=${keyword}&sortField=${sortField}&sortOrder=${sortOrder}`);
}