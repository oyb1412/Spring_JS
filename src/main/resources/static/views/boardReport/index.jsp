<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>게시글 신고</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/boardReport/style.css">
</head>
<body>

<div id="report-container">
  <h2>🚨 신고하기</h2>

  <div class="report-row">
    <label>제목</label>
    <span class="report-value">${title}</span>
  </div>

  <div class="report-row">
    <label>작성자</label>
    <span class="report-value">${writer}</span>
  </div>

  <div class="report-row">
    <label>신고자</label>
    <span class="report-value">${myWriter}</span>
  </div>

  <form action="${pageContext.request.contextPath}/board-report" method="post">
    <input type="hidden" name="reportedUserIdx" value="${reportedUserIdx}">
    <input type="hidden" name="boardIdx" value="${boardIdx}">
    <input type="hidden" name="userIdx" value="${userIdx}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

    <div class="report-row">
      <label>신고사유</label>
      <div class="report-options">
        <label><input type="radio" name="reportType" value="INSULT" required> 욕설</label>
        <label><input type="radio" name="reportType" value="PORN"> 음란성</label>
        <label><input type="radio" name="reportType" value="VIOLATION"> 정책 위반</label>
        <label><input type="radio" name="reportType" value="DEFAMATION"> 비방</label>
      </div>
    </div>

    <div class="report-row">
      <label>신고 내용</label>
      <textarea name="reportContent" rows="4" placeholder="신고 내용을 작성해주세요." required></textarea>
    </div>

    <div class="report-actions">
      <button type="submit" class="btn confirm">신고하기</button>
      <button type="button" class="btn cancel" onclick="window.close()">닫기</button>
    </div>
  </form>
</div>

<c:if test="${alreadyReported}">
  <script>
    alert("이미 신고한 게시물입니다");
    window.close();
  </script>
</c:if>


</body>
</html>
