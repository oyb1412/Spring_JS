<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../common/header.jsp" %>
<%@ include file="../common/sidebar.jsp" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ProjectsBoardList/style.css">

<div id="container">
  <div id="boardAdmin">
    <h2 id="boardAdminH2">프로젝트</h2>
  </div>

<table border="1" style="width: 100%; text-align: center; margin-top: 10px; table-layout: fixed;">
    <thead>
      <tr>
      <th style="width: 40%;">썸네일</th>
      <th style="width: 20%;">이름</th>
      <th style="width: 30%;">소개</th>
      <th style="width: 10%;">소요기간</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="projects" items="${projectsList}">
        <tr onclick="location.href='${pageContext.request.contextPath}/projects-board-check-page?idx=${projects.idx}'" style="cursor:pointer;">
          <td>${projects.thumbnail}</td>
          <td>${projects.title}</td>
          <td>${projects.introduce}</td>
          <td>${projects.indate}</td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

<%@ include file="../common/footer.jsp" %>
