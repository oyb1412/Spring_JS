<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/sidebar.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/style.css"/>

<div id="main-content">
    <h2 id="admin-title">유저 관리</h2>
    <div class="user-table-container">
        <table class="user-table">
            <thead>
                <tr>
                    <th>유저 번호</th>
                    <th>유저 이름</th>
                    <th>유저 닉네임</th>
                    <th>유저 가입일</th>
                    <th>유저 밴 상태</th>
                    <th>유저 밴 설정</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${userList}">
                    <tr>
                        <td>${user.idx}</td>
                        <td>${user.username}</td>
                        <td>${user.writer}</td>
                        <td>${user.indate}</td>
                        <td><c:choose>
                            <c:when test="${user.ban}">O</c:when>
                            <c:otherwise>X</c:otherwise>
                        </c:choose></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin-ban" method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <input type="hidden" name="userIdx" value="${user.idx}" />
                                <select name="ban" onchange="this.form.submit()">
                                    <option value="false" ${!user.ban ? 'selected' : ''}>밴 해제</option>
                                    <option value="true" ${user.ban ? 'selected' : ''}>밴</option>
                                </select>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
