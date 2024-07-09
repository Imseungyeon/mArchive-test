<!-- JSP : 서버 측에서 동작해야 할 코드들이 있을 경우 사용한다. 서버에서 보낸 데이터에 따라 값이 바뀔 수 있는 변수에 저장된 내용들을 출력할 때 사용한다.
HTML : 변화가 없는 단순 상수값을 출력할 때 사용한다. -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<head>
    <meta charset="UTF-8">
    <title>Theater Search</title>
    <link rel="stylesheet" type="text/css" href="/css/styles.css">
</head>

<body>
<div class="container">
    <div class="theater-search-container">
        <form id="theater-search-form">
            <div class="form-group theater-find">
                <label for="keyword">연극/뮤지컬 검색하기</label>
                <input type="text" class="form-control" id="keyword" name="keyword" placeholder="Enter theater title" >
                <button type="button" class="btn btn-primary theater-find" onclick="searchTheaters()" >검색</button>
            </div>
        </form>
        <div id="search-results" class="table-responsive">
            <table class="table text-center">
                <thead id="table-header" style="display: none;">
                <tr>
                    <th> 제목 </th>
                    <th> 장르 </th>
                    <th> 기간 </th>
                    <th> 장소 </th>
                    <th> 포스터 </th>
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
                searchTheaters();
            }
        });
    });

    function searchTheaters() {
        var keyword = document.getElementById("keyword").value;
        fetch("/theater/api/search", {
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

                    data.forEach(theater => {
                        var title = theater.title;
                        var genre = theater.genre;
                        var startDate = theater.startDate;
                        var endDate = theater.endDate;
                        var imageURL = theater.imageURL;
                        var place = theater.place;

                        var innerHTML = "<td>" + title + "</td>" +
                            "<td>" + genre + "</td>" +
                            "<td>" + startDate + "~" + endDate + "</td>" +
                            "<td>" + place + "</td>" +
                            '<td><img src="' + imageURL + '" alt="Book Image" width="100"/></td>';

                        var row = document.createElement("tr");
                        var theaterStringify = JSON.stringify(theater);
                        innerHTML += "<td><button onclick='selectTheater(" + theaterStringify + ")'> select </button></td>";

                        row.innerHTML = innerHTML;
                        resultsBody.appendChild(row);
                    });
                } else {
                    tableHeader.style.display = "none";
                }
            })
            .catch(error => console.error('Error: ', error));
    }

    function selectTheater(theater) {
        // 부모 창으로 선택한 연극/뮤지컬 정보 전달
        window.opener.receiveSelectedTheater(theater);
        window.close();
    }
</script>
</body>


