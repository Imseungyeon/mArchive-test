<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../layout/header.jsp"%>

<style>
    .post-info {
        font-size: 0.8em; /* 글 번호, 작성자 폰트 크기 */
        color: rgb(138, 138, 138);
    }
    .post-title {
        font-size: 1.6em; /* 글 제목 폰트 크기 */
    }
    .post-category {
        font-size: 0.8em; /* 카테고리 폰트 크기 */
        float: right; /* 페이지 오른쪽 정렬 */
        color: rgb(138, 138, 138);
        margin-top: 10px;
        margin-right: 5px;
    }
    .post-content {
        font-size: 1em; /* 글 내용 폰트 크기 */
        margin-left: 10px;
    }
    .card-header {
        font-size: 0.9em;
    }
    .reply-request {
        color: rgb(138, 138, 138);
        font-size: 0.8em;
        margin-bottom: 10px;
    }
    .reply-info{
        font-size: 0.9em;
    }
    .book-info-main{
        color: rgb(138, 138, 138);
        font-size: 0.9em;
        margin-left: 10px;
    }
    .book-title{
        font-size: 0.9em;
        margin-left: 10px;
    }
    .book-author{
        font-size: 0.9em;
        margin-left: 10px;
    }
    .book-image{
        margin-left: 10px;
    }
</style>

<div class="container">
    <button class="btn btn-secondary btn-detail" onclick="history.back()">Back</button>
    <%-- 게시글의 user와 현재 접속한 user가 동일한지 확인하여 수정/삭제 버튼 활성화 --%>
    <c:if test="${boards.user.id == principal.user.id}">
        <a href="/board/${boards.id}/updateForm" class="btn btn-modify float-right">수정</a>
        <button onclick="index.deleteById(${boards.id})" class="btn btn-delete float-right">삭제</button>
    </c:if>
    <br><br>
    <div class="post-info float-right">
        글 번호: <span id="id">${boards.id} </span>
        / 작성자: <span>${boards.user.username} </span>
    </div>
    <br>
    <div class="form-group">
        <h3 class="post-title">${boards.title}</h3>
    </div>

    <div class="form-group">
        <span class="label label-info post-category">${boards.category}</span>
    </div>
    <hr>
    <c:if test="${boards.category == 'Book'}">
        <h4 class="book-info-main">책 정보</h4>
        <div class="d-flex align-items-center">
            <img src="${boards.book.imageURL}" alt="Book Image" width="50" class="book-image">
            <p class="book-title"> ${boards.book.title}</p>
            <p class="book-author">(${boards.book.author})</p>
        </div>
    </c:if>

    <hr>
    <div class="form-group post-content">
        <div>${boards.content}</div>
    </div>
    <hr>
    <!-- 카드뷰 -->
    <div class="card">
        <form>
            <!-- 실제 ui 상에서는 userId와 boardId는 보이지 않도록 hidden 처리 -->
            <input type="hidden" id="userId" value="${principal.user.id}">
            <input type="hidden" id="boardId" value="${boards.id}">
            <div class="card-body">
                <!-- 댓글 내용 입력 창은 두 줄 -->
                <div class="reply-request">댓글 작성하기</div>
                <textarea id="reply-content" class="form-control" rows="2"></textarea>
            </div>
            <div class="card-footer d-flex justify-content-between align-items-center">
                <button type="button" id="btn-reply-save" class="btn btn-primary ml-auto">Save</button>
            </div>
        </form>
    </div>
    <br>

<%--  c:if  테스트 넣기 댓글 0개 아닐때만 노출되도록--%>

    <div class="card">
        <div class="card-header"> 댓글 </div>
        <ul id="reply-box" class="list-group">
            <c:forEach var="reply" items="${boards.replys}">
                <li id="reply-${reply.id}" class="list-group-item d-flex justify-content-between">
                    <div class="reply-info">${reply.user.username}&nbsp;&nbsp;&nbsp; ${reply.content}</div>
                    <div class="d-flex">
                        <!-- 댓글 작성자와 현재 사용자가 같으면 삭제 버튼 활성화 -->
                        <c:if test="${principal.user.username eq reply.user.username}">
                            <button onClick="index.deleteReply(${boards.id}, ${reply.id})" class="badge">Remove</button>
                        </c:if>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
    <br>

</div>
<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp" %>