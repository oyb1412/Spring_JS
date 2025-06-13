<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/boardCheck/style.css">
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/sidebar.jsp" %>


<div id="container">
	<div id="boardAdmin">
		<h2 id="boardAdminH2">프로젝트</h2>

		<label for="title">제목</label>
		<input type="text" id="title" name="title" value="${projects.title}" readonly>

        <label for="indate">소요기간</label>
		<input type="text" id="indate" name="indate" value="${projects.indate}" readonly>

		<label for="introduce">소개</label>
		<input type="text" id="introduce" name="introduce" value="${projects.introduce}" readonly>

		<label for="content">내용</label>
		<div id="content" 
     		style="border:1px solid #ccc; padding:10px; min-height:100px; background-color:white; color:black">
		<c:out value="${projects.thumbnail}" escapeXml="false" /><br><br>
  		<c:out value="${projects.content}" escapeXml="false"/>
		</div>
</div>
<%@ include file="/WEB-INF/views/common/footer.jsp" %>
