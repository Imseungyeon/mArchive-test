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
    .selected-book-image, .selected-theater-image{
        margin-left: 10px;
        margin-right: 30px;
    }
    .selected-book, .selected-theater{
        margin-top: 10px;
    }
</style>
<div class="container">
    <form>
        <div class="form-group">
            <label for="title">제목</label>
            <input type="text" class="form-control" id="title" placeholder="Enter title">
        </div>
        <div class="form-group">
            <label class="mr-sm-2" for="category">카테고리</label>
            <select class="custom-select mr-sm-2" id="category" onchange="checkCategory()">
                <option selected>Choose Category</option>
                <option value="Movie">Movie(영화)</option>
                <option value="Book">Book(도서)</option>
                <option value="Theater">Theater(연극/뮤지컬)</option>
            </select>
        </div>
        <div class="form-group" id="book-search-group" style="display: none;">
            <button type="button" class="btn btn-secondary" onclick="openBookSearch()">책 검색</button>
<%--            <input type="hidden" id="selected-book">--%>
<%--            <div id="selected-book-info"></div>--%>

            <!-- 선택한 책 정보를 표시하기 위한 요소 -->
            <div id="selected-book" class="d-flex align-items-center selected-book" style="display: none">
                <img id="book-image" class="selected-book-image" src="" width="50">
                <p id="book-title" class="mr-3 mb-0"></p>
                <p id="book-author" class="mr-3 mb-0"></p>
            </div>
        </div>

        <div class="form-group" id="movie-search-group" style="display: none;">
            <button type="button" class="btn btn-secondary" onclick="openMovieSearch()">영화 검색</button>

            <!-- 선택한 영화 정보를 표시 -->
            <div id="selected-movie" class="d-flex align-items-center selected-movie" style="display: none">
                <p id="movie-title" class="mr-3 mb-0"></p>
<%--                <p id="movie-englishTitle" class="mr-3 mb-0"></p>--%>
<%--                <p id="movie-productionYear" class="mr-3 mb-0"></p>--%>
                <p id="movie-director" class="mr-3 mb-0"></p>
                <p id="movie-genre" class="mr-3 mb-0"></p>
                <p id="movie-nation" class="mr-3 mb-0"></p>
            </div>
        </div>

        <div class="form-group" id="theater-search-group" style="display: none;">
            <button type="button" class="btn btn-secondary" onclick="openTheaterSearch()">연극/뮤지컬 검색</button>

            <!-- 선택한 연극/뮤지컬 정보를 표시하기 위한 요소 -->
            <div id="selected-theater" class="d-flex align-items-center selected-theater" style="display: none">
                <img id="theater-image" class="selected-theater-image" src="" width="50">
                <p id="theater-title" class="mr-3 mb-0"></p>
                <p id="theater-genre" class="mr-3 mb-0"></p>
                <p id="theater-period" class="mr-3 mb-0"></p>
                <p id="theater-place" class="mr-3 mb-0"></p>
            </div>
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
            document.getElementById("theater-search-group").style.display = "none";
            document.getElementById("movie-search-group").style.display = "none";
            document.getElementById("book-search-group").style.display = "block";
            var bookData = document.getElementById("selected-book").dataset.book;
            if (bookData && bookData !== "null") {
                document.getElementById("selected-book").style.display = "flex";
            } else {
                document.getElementById("selected-book").style.display = "none";
            }
        } else if (category === "Movie") {
            document.getElementById("book-search-group").style.display = "none";
            document.getElementById("movie-search-group").style.display = "block";
            document.getElementById("theater-search-group").style.display = "none";
            var movieData = document.getElementById("selected-movie").dataset.movie;
            if (movieData && movieData !== "null") {
                document.getElementById("selected-movie").style.display = "flex";
            } else {
                document.getElementById("selected-movie").style.display = "none";
            }
        }  else if (category === "Theater") {
            document.getElementById("theater-search-group").style.display = "block";
            document.getElementById("movie-search-group").style.display = "none";
            document.getElementById("book-search-group").style.display = "none";
            var theaterData = document.getElementById("selected-theater").dataset.theater;
            if (theaterData && theaterData !== "null") {
                document.getElementById("selected-theater").style.display = "flex";
            } else {
                document.getElementById("selected-theater").style.display = "none";
            }

        }else {
            document.getElementById("book-search-group").style.display = "none";
            document.getElementById("selected-book").style.display = "none";
            document.getElementById("selected-book").dataset.book = null;

            document.getElementById("movie-search-group").style.display = "none";
            document.getElementById("selected-movie").style.display = "none";
            document.getElementById("selected-movie").dataset.movie = null;

            document.getElementById("theater-search-group").style.display = "none";
            document.getElementById("selected-theater").style.display = "none";
            document.getElementById("selected-theater").dataset.movie = null;
        }
    }

    function openBookSearch() {
        window.open("/book/search", "Book Search", "width=800,height=600");
    }

    function openMovieSearch() {
        window.open("/movie/search", "Movie Search", "width=800,height=600");
    }

    function openTheaterSearch() {
        window.open("/theater/search", "Theater Search", "width=800,height=600");
    }

    function receiveSelectedBook(book) {
        document.getElementById("selected-book").style.display = "block";
        document.getElementById("book-title").innerText = book.title;
        document.getElementById("book-author").innerText = "(" + book.author + ")";
        document.getElementById("book-image").src = book.imageURL;
        document.getElementById("selected-book").dataset.book = JSON.stringify(book);
    }

    function receiveSelectedMovie(movie) {
        document.getElementById("selected-movie").style.display = "block";
        document.getElementById("movie-title").innerText = movie.title + "(" + movie.englishTitle + ", " + movie.productionYear + ")";
        document.getElementById("movie-director").innerText = movie.director;
        document.getElementById("movie-genre").innerText = movie.genre;
        document.getElementById("movie-nation").innerText = movie.nation;
        document.getElementById("selected-movie").dataset.movie = JSON.stringify(movie);
    }

    function receiveSelectedTheater(theater) {
        document.getElementById("selected-theater").style.display = "block";
        document.getElementById("theater-title").innerText = theater.title;
        document.getElementById("theater-genre").innerText = theater.genre;
        document.getElementById("theater-period").innerText = theater.startDate + " ~ " + theater.endDate;
        document.getElementById("theater-place").innerText = theater.place;
        document.getElementById("theater-image").src = theater.imageURL;
        document.getElementById("selected-theater").dataset.theater = JSON.stringify(theater);
    }


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