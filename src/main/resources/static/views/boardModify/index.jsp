<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/boardModify/style.css">
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/sidebar.jsp" %>

<form action="${pageContext.request.contextPath}/board-modify" method="post">
  <div id="container">
    <div id="boardAdmin">
      <h2 id="boardAdminH2">글 수정</h2>

      <input type="hidden" id="idx" name="idx" value="${board.idx}" readonly>

      <label for="memID">회원아이디</label>
      <input type="text" id="memID" name="memID" value="${board.memID}" readonly>

      <label for="title">제목</label>
      <input type="text" id="title" name="title" value="${board.title}">

      <label for="content">내용</label>
      <textarea id="content" name="content" rows="5" maxlength="1000">${board.content}</textarea>

      <label for="writer">작성자</label>
      <input type="text" id="writer" name="writer" value="${board.writer}" readonly>

      <label for="indate">작성일</label>
      <input type="text" id="indate" name="indate" value="${board.indate}" readonly>

      <label for="viewCount">조회수</label>
      <input type="text" id="viewCount" name="viewCount" value="${board.viewCount}" readonly>

      <div id="buttonContainer">
        <c:if test="${MANAGER == true || ADMIN == true || board.memID == username}">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
          <input type="hidden" name="boardType" value="FREE" />
          <button type="submit" class="btn update" onclick="return confirm('정말 수정하시겠습니까?')">확인</button>
        </c:if>

          <button type="button" class="btn cancel" onclick="location.href='${pageContext.request.contextPath}/board-list'">취소</button>

      </div>
    </div>
  </div>
</form>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote-lite.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote-lite.min.css" rel="stylesheet">
<script>
  $(document).ready(function () {
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
</script>
