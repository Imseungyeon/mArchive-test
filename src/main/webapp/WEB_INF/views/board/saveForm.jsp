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
            <select class="custom-select mr-sm-2" id="category" onchange="checkCategory()">
                <option selected>Choose Category</option>
                <option value="Movie">Movie</option>
                <option value="Book">Book</option>
                <option value="Exhibition">Exhibition</option>
            </select>
        </div>
        <div class="form-group" id="book-search-group" style="display: none;">
            <button type="button" class="btn btn-secondary" onclick="openBookSearch()">Search Book</button>
            <input type="hidden" id="selected-book">
            <div id="selected-book-info"></div>
        </div>
        <div class="form-group">
            <!-- summernote 활용 -->
            <textarea class="form-control summernote" rows="5" id="content"></textarea>
        </div>
    </form>
    <button id="btn-save" class="btn btn-primary">Save</button>
</div>

<script>
    function checkCategory() {
        var category = document.getElementById("category").value;
        if (category === "Book") {
            document.getElementById("book-search-group").style.display = "block";
        } else {
            document.getElementById("book-search-group").style.display = "none";
        }
    }

    function openBookSearch() {
        window.open("/book/search", "Book Search", "width=800,height=600");
    }

    function receiveSelectedBook(book) {
        document.getElementById("selected-book").value = JSON.stringify(book);
        var bookInfoDiv = document.getElementById("selected-book-info");
        bookInfoDiv.innerHTML = `<h5>${book.title}</h5><p>${book.author}</p><img src="${book.imageURL}" />`;
    }

    $(document).ready(function() {
        $('#btn-save').on("click", function() {
            var data = {
                title: $("#title").val(),
                category: $("#category").val(),
                content: $("#content").val(),
                book: JSON.parse($("#selected-book").val())
            };

            $.ajax({
                type: "POST",
                url: "/api/board",
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
            }).done(function(resp) {
                alert("Success Save Post");
                location.href = "/";
            }).fail(function(error) {
                alert("Failed Save Post");
            });
        });
    });


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