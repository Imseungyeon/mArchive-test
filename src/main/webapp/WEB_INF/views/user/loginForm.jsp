<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../layout/header.jsp" %>


<div class="container">
    <form action="/auth/loginProc" method="post">
        <div class="form-group">
            <label for="username">아이디</label>
            <input type="username" name="username" class="form-control" placeholder="Enter your id" id="username">
        </div>

        <div class="form-group">
            <label for="password">비밀번호</label>
            <input type="Password" name="password" class="form-control" placeholder="Enter your password" id="password">
        </div>
        <c:if test="${not empty error}">
        <span>
            <p id="valid" class="alert alert-danger">
                ${exception}
            </p>
        </span>
        </c:if>
        <button id="btn-login" class="btn btn-primary">로그인</button>
    </form>
    <hr>
    <div class="text-center mt-3">
        <h5 class="OAuth-login-header">소셜 로그인</h5>
        <div class="mt-2">
            <a href="/oauth2/authorization/naver" class="btn btn-outline-success btn-block">
                <i class="fab fa-neversoft"></i> 네이버 계정으로 로그인
            </a>
        </div>
    </div>
</div>
</div>
</div>
<%@ include file="../layout/footer.jsp" %>