<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="layout/header.jsp" %>

<div class="container">
    <%-- 게시글 리스트 출력 - jstl for문 --%>
    <c:forEach var="boards" items="${boards.content}">
        <div class="card m-2">
            <div class="card-body">
                <h4 class="card-title">${boards.title}</h4>
                <a href="/board/${boards.id}" class="btn btn-primary">Detail</a>
            </div>
        </div>
    </c:forEach>

   <%-- 페이지 네이션 동작 --%>
   <ul class="pagination justify-content-center">
       <c:choose>
           <c:when test="${boards.first}">
                <%-- 처음 부분은 이전 버튼 비활성화 --%>
                <li class="page-item disabled"><a class="page-link" href="?page=${boards.number-1}">Previous</a></li>
           </c:when>
           <c:otherwise>
                <li class="page-item"><a class="page-link" href="?page=${boards.number-1}">Previous</a></li>
           </c:otherwise>
      </c:choose>
      <c:choose>
           <c:when test="${boards.last}">
               <%-- 마지막 부분은 다음 버튼 비활성화 --%>
                <li class="page-item disabled"><a class="page-link" href="?page=${boards.number+1}">Next</a></li>
           </c:when>
           <c:otherwise>
                <li class="page-item"><a class="page-link" href="?page=${boards.number+1}">Next</a></li>
           </c:otherwise>
      </c:choose>
   </ul>
</div>
<%@ include file="layout/footer.jsp" %>
