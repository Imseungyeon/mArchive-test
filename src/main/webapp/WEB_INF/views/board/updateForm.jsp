<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../layout/header.jsp" %>
<style>
    .d-flex {
        display: flex;
    }
    .align-items-center {
        align-items: center;
    }
    .mr-3 {
        margin-right: 1rem;
    }
    .mb-0 {
        margin-bottom: 0;
    }
    .selected-book-image{
        margin-left: 10px;
        margin-right: 30px;
    }
    .selected-book{
        margin-top: 10px;
    }
</style>
<div class="container">
    <form>
        <!-- 게시글의 id를 hidden 형식으로 보이지 않게 -->
        <input type="hidden" id="id" value="${boards.id}">
        <div class="form-group">
            <!-- 여기 확인 제목은 이미 있는 값을 가져와서 해당 필드에 출력 -->
            <input value="${boards.title}" type="text" name="title" class="form-control" placeholder="Enter title" id="title">
        </div>
        <div class="form-group">
            <label class="mr-sm-2" for="selectCategory">Category</label>
            <select class="custom-select mr-sm-2" id="selectCategory">
                <!-- 이미 카테고리를 선택했으므로 변경할 수 없도록-->
                <option selected>${boards.category}</option>
            </select>
        </div>

        <c:if test="${boards.category == 'Book'}">
            <div class="form-group" id="book-search-group">
                <div id="selected-book" class="d-flex align-items-center selected-book">
                    <input type="hidden" id="book-apiId" value="${boards.book.apiId}">
                    <img id="book-image" class="selected-book-image" src="${boards.book.imageURL}" alt="Book Image" width="50">
                    <p id="book-title" class="mr-3 mb-0">${boards.book.title}</p>
                    <p id="book-author" class="mr-3 mb-0">(${boards.book.author})</p>
                </div>
            </div>
        </c:if>

        <div class="form-group">
            <textarea class="form-control summernote" rows="5" id="content">${boards.content}</textarea>
        </div>
    </form>
    <button id="btn-update" class="btn btn-primary">Save</button>

</div>

<script>

    $('.summernote').summernote({
        height: 300,
        minHeight: null,
        maxHeight: null,
        focus: true,
        lang: "ko-KR",
        placeholder: '최대 2048자까지 작성할 수 있습니다.',
        pasteHTML: '${boards.content}' //이전에 saveForm에서 html 형태로 저장했기 때문에 html 태그가 같이 출력될 수 있으므로 pasteHTML로 태그 제거
    });
</script>

<script src="/js/board.js"></script>
<%@include file="../layout/footer.jsp"%>