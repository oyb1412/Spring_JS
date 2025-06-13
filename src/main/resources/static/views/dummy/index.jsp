<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${alreadyReported}">
  <script>
    alert("이미 신고한 게시물입니다");
    window.close();
  </script>
</c:if>

<c:if test="${reportFail}">
  <script>
    alert("신고에 실패했습니다");
    window.close();
  </script>
</c:if>

<c:if test="${reportSuccess}">
  <script>
    alert("신고에 성공했습니다");
    window.close();
  </script>
</c:if>