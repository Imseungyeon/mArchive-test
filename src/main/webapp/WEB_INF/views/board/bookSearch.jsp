<!-- JSP : 서버 측에서 동작해야 할 코드들이 있을 경우 사용한다. 서버에서 보낸 데이터에 따라 값이 바뀔 수 있는 변수에 저장된 내용들을 출력할 때 사용한다.
HTML : 변화가 없는 단순 상수값을 출력할 때 사용한다. -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="container">
    <form id="book-search-form">
        <div class="form-group">
            <label for="keyword">Book Search</label>
            <input type="text" class="form-control" id="keyword" name="keyword" placeholder="Enter book title">
        </div>
        <button type="button" class="btn btn-primary" onclick="searchBooks()">Search</button>
    </form>
    <div id="search-results"></div>
</div>

<script>
    function searchBooks() {
        var keyword = document.getElementById("keyword").value;
        fetch("/book/api/search", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ keyword: keyword })
        })
            .then(response => response.json())
            .then(data => {
                console.log("ReceivedData: ", data);
                var resultsDiv = document.getElementById("search-results");
                resultsDiv.innerHTML = "";
                data.forEach(book => {
                    var title = book.title;
                    var author = book.author;
                    var imageURL = book.imageURL;
                    var innerHTML = "<h5>" + title + "</h5>" + "<p>" + author + "</p>" + '<img src="' + imageURL + '" alt="Book Image"/>';
                    var bookDiv = document.createElement("div");
                    <%--var innerHTML = `<h5>${book.title}</h5> <p>${book.author}</p> <img src="${book.imageURL}" alt="Book Image"/> `;--%>
                    //<button onclick='selectBook(${JSON.stringify(book)})'>Select</button>

                    //innerHTML 대신 insertAdjactHTML 사용
                    // bookDiv.insertAdjacentHTML()
                    // console.log(innerHTML);
                    bookDiv.innerHTML = innerHTML;
                    resultsDiv.appendChild(bookDiv);
                });
            })
            .catch(error => console.error('Error: ', error));
    }

    function selectBook(book) {
        // 부모 창으로 선택한 책 정보 전달
        window.opener.receiveSelectedBook(book);
        window.close();
    }
</script>

<%@ include file="../layout/footer.jsp" %>
