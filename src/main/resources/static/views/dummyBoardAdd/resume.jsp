<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/sidebar.jsp" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/boardAdd/style.css">
<form action="${pageContext.request.contextPath}/dummy-board-add" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

    <div id="container">
        <div id="boardAdmin">
            <h2 id="boardAdminH2">글 작성</h2>

            <label for="memID">회원아이디</label>

            <label for="title">제목</label>
            <input type="text" id="title" name="title" placeholder="제목" maxlength="10">

            <div class="editor-tabs">
              <button type="button" class="tab-button active" id="write-tab">작성</button>
              <button type="button" class="tab-button" id="preview-tab">프리뷰</button>
            </div>

            <label for="content">내용</label>
              <textarea id="content" name="content" placeholder="내용을 입력하세요"
              rows="5" cols="40" maxlength="1000"></textarea>

              <div id="preview-area" style="display:none;"></div>

            <button id="buttonSubmit" type="submit">확인</button>
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

<script>
  document.addEventListener('DOMContentLoaded', function () {
    const writeTab = document.getElementById('write-tab');
    const previewTab = document.getElementById('preview-tab');
    const textarea = document.getElementById('content');
    const previewArea = document.getElementById('preview-area');

    writeTab.addEventListener('click', function () {
      writeTab.classList.add('active');
      previewTab.classList.remove('active');

      // show summernote
      $('#content').next('.note-editor').show();
      previewArea.style.display = 'none';

      // 쪼그라듬 방지 (내용 보존)
      setTimeout(() => {
        $('.note-editable').css('min-height', '300px');
      }, 0);
    });

    previewTab.addEventListener('click', function () {
      previewTab.classList.add('active');
      writeTab.classList.remove('active');

      const html = $('#content').summernote('code');

      const imgRegex = /!\[.*?\]\((.*?)\)/g;
      const converted = html.replace(imgRegex, (match, url) => {
        return `<img src="${url}" style="max-width:100%; margin:10px 0;">`;
      });

      previewArea.innerHTML = converted;

      // hide summernote
      $('#content').next('.note-editor').hide();
      previewArea.style.display = 'block';
    });
  });
</script>