<!-- JSP : 서버 측에서 동작해야 할 코드들이 있을 경우 사용한다. 서버에서 보낸 데이터에 따라 값이 바뀔 수 있는 변수에 저장된 내용들을 출력할 때 사용한다.
HTML : 변화가 없는 단순 상수값을 출력할 때 사용한다. -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<head>
    <meta charset="UTF-8">
    <title>Book Search</title>
    <link rel="stylesheet" type="text/css" href="/css/styles.css">
</head>

<body>
<div class="container">
    <div class="book-search-container">
        <form id="book-search-form">
            <div class="form-group book-find">
                <label for="keyword">책 검색하기</label>
                <input type="text" class="form-control" id="keyword" name="keyword" placeholder="Enter book title">
                <button type="button" class="btn btn-primary book-find" onclick="searchBooks()">검색</button>
            </div>
<%--        <button type="button" class="btn btn-primary book-find" onclick="searchBooks()">검색</button>--%>
        </form>
        <div id="search-results" class="table-responsive">
            <table class="table text-center">
                <thead id="table-header" style="display: none;">
                <tr>
                    <th> 책 제목 </th>
                    <th> 저자 </th>
                    <th> 표지 </th>
                    <th> 선택 </th>
                </tr>
            </thead>
            <tbody id="results-body">
                <!-- 검색 결과가 여기에 추가됩니다. -->
            </tbody>
        </table>
    </div>
</div>
</div>


<script>
    function searchBooks() {
        var keyword = document.getElementById("keyword").value;
        fetch("/book/api/search", {
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

                    data.forEach(book => {
                        var title = book.title;
                        var author = book.author;
                        var imageURL = book.imageURL;
                        var innerHTML = "<td>" + title + "</td>" + "<td>" + author + "</td>" + '<td><img src="' + imageURL + '" alt="Book Image" width="100"/></td>';

                        var row = document.createElement("tr");
                        <%--var innerHTML = `<h5>${book.title}</h5> <p>${book.author}</p> <img src="${book.imageURL}" alt="Book Image"/> `;--%>
                        innerHTML += "<td><button onclick='selectBook(${JSON.stringify(book)})'> select </button></td>";

                        // innerHTML 대신 insertAdjactHTML 사용
                        // bookDiv.insertAdjacentHTML()
                        // console.log(innerHTML);
                        // bookDiv.innerHTML = innerHTML;
                        // resultsDiv.appendChild(bookDiv);

                        row.innerHTML = innerHTML;
                        resultsBody.appendChild(row);
                    });
                } else {
                    tableHeader.style.display = "none";
                }
            })
            .catch(error => console.error('Error: ', error));
    }

    function selectBook(book) {
        // 부모 창으로 선택한 책 정보 전달
        window.opener.receiveSelectedBook(book);
        window.close();
    }
</script>
</body>


