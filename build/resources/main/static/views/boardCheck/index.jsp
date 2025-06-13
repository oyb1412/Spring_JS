<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/boardCheck/style.css">
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/sidebar.jsp" %>

<!-- 이 아래부터 진짜 컨텐츠만 들어가야 함 -->

<div id="container">
	<div id="boardAdmin">
		<h2 id="boardAdminH2">글 조회</h2>

		<label for="title">제목</label>
		<input type="text" id="title" name="title" value="${board.title}" readonly>

		<label for="content">내용</label>
		<div id="content" 
     		style="border:1px solid #ccc; padding:10px; min-height:100px; background-color:white; color:black">
  		<c:out value="${board.content}" escapeXml="false"/>
		</div>

		<label for="writer">작성자</label>
		<input type="text" id="writer" name="writer" value="${board.writer}" readonly>

		<label for="indate">작성일</label>
		<input type="text" id="indate" name="indate" value="${board.indate}" readonly>

		<label for="viewCount">조회수</label>
		<input type="text" id="viewCount" name="viewCount" value="${board.viewCount}" readonly>

<div id="vote-section">
  <form action="${pageContext.request.contextPath}/board-vote" method="post" style="display: inline;">
    <input type="hidden" name="idx" value="${board.idx}" />
    <input type="hidden" name="voteType" value="up" />
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <button type="submit" class="vote-btn up">★<br>좋아요</button>
  </form>
  <span class="vote-count">${board.upCount}</span>

  <form action="${pageContext.request.contextPath}/board-vote" method="post" style="display: inline;">
    <input type="hidden" name="idx" value="${board.idx}" />
    <input type="hidden" name="voteType" value="down" />
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <button type="submit" class="vote-btn down">⬇<br>싫어요</button>
  </form>
  <span class="vote-count">${board.downCount}</span>

  <c:if test="${isAuthenticated}">
  <button type="button" class="btn report-btn"
          onclick="openReportPopup('${board.idx}' , '${board.userIdx}', '${board.writer}', '${board.title}')">🚨<br>신고</button>
</c:if>
</div>

		<div id="buttonContainer">
			<c:if test="${MANAGER == true || ADMIN == true || board.memID == username}">
				<form action="${pageContext.request.contextPath}/board-modify-page" method="get">
					<input type="hidden" name="idx" value="${board.idx}">
					<button type="submit" class="btn update">수정</button>
				</form>
				<form action="${pageContext.request.contextPath}/board-delete" method="post">
					<input type="hidden" name="idx" value="${board.idx}">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					<button type="submit" class="btn delete" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</button>
				</form>
			</c:if>
		</div>
	</div>

	<div id="comment-section">
		<h3>댓글</h3>
		<c:forEach var="comment" items="${comments}">
  <div class="comment-box" style="position: relative;">
    <strong>${comment.writer}</strong>
    <span class="comment-date">(${comment.indate})</span>

    <c:if test="${writer == comment.writer}">
      <form action="${pageContext.request.contextPath}/comment-delete" method="post"
            style="position: absolute; top: 0; right: 0;">
        <input type="hidden" name="idx" value="${comment.idx}" />
        <input type="hidden" name="parentIdx" value="${comment.parentIdx}" />

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <button type="submit" style="
            background: none;
            border: none;
            color: red;
            font-size: 30px;
            cursor: pointer;
        " onclick="return confirm('댓글을 삭제하시겠습니까?')">🗑</button>
      </form>
    </c:if>

    <p>${comment.content}</p>
  </div>
</c:forEach>

		<form action="${pageContext.request.contextPath}/comment-add" method="post">
			<input type="hidden" name="parentIdx" value="${board.idx}" />
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			<input type="hidden" name="boardType" value="FREE">

			<c:if test="${isAuthenticated}">
				<textarea name="content" placeholder="댓글을 입력하세요" rows="4"></textarea>
			</c:if>

			<c:if test="${not isAuthenticated}">
				<textarea name="content" placeholder="로그인 해주시길 바랍니다" rows="4" readonly></textarea>
			</c:if>
			

			<div class="comment-submit">
	<c:choose>
		<c:when test="${isAuthenticated}">
			<button type="submit" class="btn">등록</button>
		</c:when>
		<c:otherwise>
			<button type="submit" class="btn" disabled>등록</button>
		</c:otherwise>
	</c:choose>
</div>
</div>
		</form>

		<p class="comment-warning">
			명예훼손, 개인정보 유출, 분쟁 유발, 허위사실 유포 등은 이용약관에 의해 제재될 수 있으며,
			관련 법률에 의해 처벌 받을 수 있습니다. 건전한 커뮤니티를 위해 자제를 당부 드립니다.
		</p>
	</div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

<script>
function openReportPopup(boardIdx, reportedUserIdx, writer, title) {
    const url = '/board-report-page?boardIdx=' + encodeURIComponent(boardIdx)
              + '&reportedUserIdx=' + encodeURIComponent(reportedUserIdx)
              + '&writer=' + encodeURIComponent(writer)
              + '&title=' + encodeURIComponent(title);

    window.open(url, 'reportWindow', 'width=500,height=400,resizable=no');
}
</script>