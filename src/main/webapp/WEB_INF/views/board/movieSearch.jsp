<!-- JSP : 서버 측에서 동작해야 할 코드들이 있을 경우 사용한다. 서버에서 보낸 데이터에 따라 값이 바뀔 수 있는 변수에 저장된 내용들을 출력할 때 사용한다.
HTML : 변화가 없는 단순 상수값을 출력할 때 사용한다. -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<head>
    <meta charset="UTF-8">
    <title>Movie Search</title>
    <link rel="stylesheet" type="text/css" href="/css/styles.css">
</head>

<body>
<div class="container">
    <div class="movie-search-container">
        <form id="movie-search-form">
            <div class="form-group movie-find">
                <label for="keyword">영화 검색하기</label>
                <input type="text" class="form-control" id="keyword" name="keyword" placeholder="Enter movie title" >
                <button type="button" class="btn btn-primary movie-find" onclick="searchMovies()" >검색</button>
            </div>
        </form>
        <div id="search-results" class="table-responsive">
            <table class="table text-center">
                <thead id="table-header" style="display: none;">
                <tr>
                    <th> 영화 제목(영어 제목, 개봉년도) </th>
                    <th> 감독 </th>
                    <th> 장르 </th>
                    <th> 국가 </th>
                    <th> 선택 </th>
                </tr>
                </thead>
                <tbody id="results-body">
                <!-- 검색 결과 추가되는 곳-->
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        document.getElementById("keyword").addEventListener("keydown", function(event) {
            if (event.key === "Enter") {
                event.preventDefault();
                searchMovies();
            }
        });
    });

    function searchMovies() {
        var keyword = document.getElementById("keyword").value;
        fetch("/movie/api/search", {
            method: "POST",
            headers: {
                "Content-Type": "application/json; charset=UTF-8"
            },
            body: JSON.stringify({ keyword: keyword })
        })
            .then(response => response.json())
            .then(data => {
                console.log("ReceivedData: ", data);
                var resultsBody = document.getElementById("results-body");
                var tableHeader = document.getElementById("table-header");
                resultsBody.innerHTML = "";
                if (data.length > 0){
                    // 검색 결과 있을 때 헤더 표시
                    tableHeader.style.display = "";

                    data.forEach(movie => {
                        var title = movie.title;
                        var englishTitle = movie.englishTitle;
                        var productionYear = movie.productionYear;
                        var director = movie.director;
                        var genre = movie.genre;
                        var nation = movie.nation;

                        var innerHTML = "<td>" + title + "(" + englishTitle + "," + productionYear + ")" + "</td>" +
                                        "<td>" + director + "</td>" +
                                        "<td>" + genre + "</td>" +
                                        "<td>" + nation + "</td>";

                        var row = document.createElement("tr");
                        var movieStringify = JSON.stringify(movie);
                        innerHTML += "<td><button onclick='selectMovie(" + movieStringify + ")'> select </button></td>";

                        row.innerHTML = innerHTML;
                        resultsBody.appendChild(row);
                    });
                } else {
                    tableHeader.style.display = "none";
                }
            })
            .catch(error => console.error('Error: ', error));
    }

    function selectMovie(movie) {
        // 부모 창으로 선택한 영화 정보 전달
        window.opener.receiveSelectedMovie(movie);
        window.close();
    }
</script>
</body>


