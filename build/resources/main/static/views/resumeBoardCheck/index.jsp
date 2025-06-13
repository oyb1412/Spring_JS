<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/sidebar.jsp" %>

<!-- CSS 링크 추가 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/resumeBoardCheck/style.css">

<div id="resume-container">
  <h2 class="resume-title">오윤범 이력서</h2>
  <div id="resume-content">${resume.content}</div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>