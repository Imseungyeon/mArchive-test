<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../layout/header.jsp" %>

<div class="container">
    <form>
        <div class="form-group">
            <label for="title">Title</label>
            <input type="text" class="form-control" id="title" placeholder="Enter title">
        </div>
        <div class="form-group">
            <label class="mr-sm-2" for="category">Category</label>
            <select class="custom-select mr-sm-2" id="category">
                <option selected>Choose Category</option>
                <option value="Movie">Movie</option>
                <option value="Book">Book</option>
                <option value="Exhibition">Exhibition</option>
            </select>
        </div>
        <div class="form-group">
            <!-- summernote 활용 -->
            <textarea class="form-control summernote" rows="5" id="content"></textarea>
        </div>
    </form>
    <button id="btn-save" class="btn btn-primary">Save</button>
</div>

<script>
    $('.summernote').summernote({
        height: 300,
        minHeight: null,
        maxHeight: null,
        focus: true,
        lang: "ko-KR",
        placeholder: '최대 2048자까지 작성할 수 있습니다.'
    });
</script>

<script src="/js/board.js"></script>
<%@include file="../layout/footer.jsp"%>