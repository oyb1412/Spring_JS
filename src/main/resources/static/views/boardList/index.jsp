<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../common/header.jsp" %>
<%@ include file="../common/sidebar.jsp" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/boardList/style.css">

<div id="container">
  <div id="boardAdmin">
    <h2 id="boardAdminH2">자유게시판</h2>
    

    <c:if test="${isAuthenticated}">
      <button type="button" onclick="location.href='${pageContext.request.contextPath}/board-add-page'">작성</button>
    </c:if>
  </div>

  <table border="1" style="width: 100%; text-align: center; margin-top: 10px;">
    <thead>
      <tr>
        <th>번호</th>
        <th>제목</th>
        <th>글쓴이</th>
        <th>등록일</th>
        <th>조회</th>
        <th>댓글</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="board" items="${boardList}">
        <tr onclick="location.href='${pageContext.request.contextPath}/board-check-page?idx=${board.idx}'" style="cursor:pointer;">
          <td>${board.idx}</td>
          <td>${board.title}</td>
          <td>${board.writer}</td>
          <td>${board.indate}</td>
          <td>${board.viewCount}</td>
          <td>${board.commentCount}</td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

<div id="searchBox">
  <form action="${pageContext.request.contextPath}/board-list-page" method="get" style="display: flex; justify-content: center; align-items: center; margin-top: 20px; gap: 10px;">
    <select name="searchType">
      <option value="title" ${param.searchType == 'title' ? 'selected' : ''}>제목</option>
      <option value="content" ${param.searchType == 'content' ? 'selected' : ''}>내용</option>
      <option value="writer" ${param.searchType == 'writer' ? 'selected' : ''}>글쓴이</option>
      <option value="title_content" ${param.searchType == 'title_content' ? 'selected' : ''}>제목+내용</option>
    </select>
    <input type="text" name="keyword" value="${param.keyword}" placeholder="검색어를 입력하세요" />
    <button type="submit">🔍</button>
  </form>
</div>

<form id="sortForm" method="get" action="${pageContext.request.contextPath}/board-list-page">
  <input type="hidden" name="sortField" id="sortField" value="default">
  <input type="hidden" name="sortOrder" id="sortOrder" value="desc">
  <input type="hidden" name="searchType" value="${param.searchType}">
  <input type="hidden" name="keyword" value="${param.keyword}">
  <input type="hidden" name="page" value="${param.page}">

  <div id="sort-options">
    <button type="button" class="sort-btn" onclick="setSort('view');">조회수 ▲</button>
    <button type="button" class="sort-btn" onclick="setSort('comment');">댓글수 ▲</button>
    <button type="button" class="sort-btn" onclick="setSort('date');">작성일 ▲</button>
  </div>
</form>


  <div class="pagination">

  <c:if test="${currentBoardPage > 1}">
    <a href="${pageContext.request.contextPath}/board-list-page?page=${currentBoardPage - 1}&searchType=${param.searchType}&keyword=${param.keyword}
    &sortField=${param.sortField}&sortOrder=${param.sortOrder}">이전</a>
  </c:if>

  <c:forEach begin="1" end="${totalBoardPage}" var="i">
    <a href="${pageContext.request.contextPath}/board-list-page?page=${i}&searchType=${param.searchType}&keyword=${param.keyword}
    &sortField=${param.sortField}&sortOrder=${param.sortOrder}"
       style="${i == currentBoardPage ? 'font-weight: bold;' : ''}">${i}</a>
  </c:forEach>

  <c:if test="${currentBoardPage < totalBoardPage}">
    <a href="${pageContext.request.contextPath}/board-list-page?page=${currentBoardPage + 1}&searchType=${param.searchType}&keyword=${param.keyword}
    &sortField=${param.sortField}&sortOrder=${param.sortOrder}">다음</a>
  </c:if>

</div>

<%@ include file="../common/footer.jsp" %>

<script>
let urlParams = new URLSearchParams(window.location.search);
let sortField = urlParams.get("sortField") || "default";
let sortOrder = urlParams.get("sortOrder") || "desc";

let sortStates = {
  view: 'desc',
  comment: 'desc',
  date: 'desc'
};

// 정렬 버튼 클릭 시 실행
function setSort(type) {
  // 이전 정렬 상태 반영
  if (sortField !== "default" && sortStates[sortField]) {
    sortStates[sortField] = sortOrder;
  }

  // 현재 정렬 방향 토글
  sortStates[type] = sortStates[type] === 'desc' ? 'asc' : 'desc';

  // form 값 세팅
  document.getElementById('sortField').value = type;
  document.getElementById('sortOrder').value = sortStates[type];

  // 버튼 텍스트 업데이트
  updateSortButtonArrows();

  // 폼 제출
  document.getElementById('sortForm').submit();
}

// 페이지 로드 시 화살표 복원
window.addEventListener('DOMContentLoaded', () => {
  updateSortButtonArrows();
});

// 화살표 업데이트 함수
function updateSortButtonArrows() {
  const arrows = { asc: '▼', desc: '▲' };

  document.querySelectorAll('.sort-btn').forEach(btn => {
    if (btn.textContent.includes('조회수')) {
      btn.textContent = '조회수 ' + arrows[(sortField === 'view') ? sortOrder : sortStates.view];
    }
    if (btn.textContent.includes('댓글수')) {
      btn.textContent = '댓글수 ' + arrows[(sortField === 'comment') ? sortOrder : sortStates.comment];
    }
    if (btn.textContent.includes('작성일')) {
      btn.textContent = '작성일 ' + arrows[(sortField === 'date') ? sortOrder : sortStates.date];
    }
  });
}
</script>