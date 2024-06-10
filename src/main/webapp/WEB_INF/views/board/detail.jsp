<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../layout/header.jsp"%>

<div class="container">
    <button class="btn btn-secondary" onclick="history.back()">Back</button>
    <%-- 게시글의 user와 현재 접속한 user가 동일한지 확인하여 버튼 활성화 --%>
    <c:if test="${boards.user.id == principal.user.id}">
        <a href="/board/${boards.id}/updateForm" class="btn btn-warning">Modify</a>
        <button onclick="index.deleteById(${boards.id})" class="btn btn-danger">Delete</button>
    </c:if>
    <br><br>
    <div>
        Post Id: <span id="id"><i>${boards.id}</i></span>
        Post Writer: <span><i>${boards.user.username} </i></span>
    </div>
    <br>
    <div class="form-group">
        <h3>${boards.title}</h3>
    </div>

    <div class="form-group">
        <span class="label label-info">${boards.category}</span>
    </div>
    <hr>
    <div class="form-group">
        <div>${boards.content}</div>
    </div>

</div>
<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp" %>