//백엔드로 api 호출하는 로직
let index = {
    //초기화 - 버튼별 동작할 함수 맵핑
    //delete는 js 함수 호출할 것이라 제외
    init: function() {
        $("#btn-save").on("click", () =>{
            this.save();
        });
        $("#btn-update").on("click", () =>{
            this.update();
        });
        $("#btn-reply-save").on("click", () =>{
            this.saveReply();
        });
    },

    //글 저장
    save: function() {
        let data = {
            title: $("#title").val(),
            category: $("#category").val(),
            content: $("#content").val(),
        }

        $.ajax({
            type: "POST",
            url: "/api/board",
            data: JSON.stringify(data), //json 타입
            contentType: "application/json; charset=utf-8",
        }).done(function(resp) {
            alert("Success Save Post");
            location.href = "/"; //메인 페이지로 리다이렉션
        }).fail(function(error) {
            alert("Failed Save Post");
        })
    },

    //업데이트
    update: function() {
        //id값 가져와야 함
        let id = $("#id").val();
        let data = {
            title: $("#title").val(),
            category: $("#category").val(),
            content: $("#content").val(),
        }

        $.ajax({
            type: "PUT",
            url: "/api/board/" + id,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
        }).done(function(resp) {
            alert("Success Update Post");
            location.href = "/";
        }).fail(function(error) {
            alert("Failed Update Post");
        })
    },

    //게시글 삭제
    deleteById : function(boardId) {
        $.ajax({
            type: "DELETE",
            url: `/api/board/${boardId}`,
            contentType: "application/json; charset=utf-8",
        }).done(function(resp) {
            alert("Success Delete Post");
            location.href = "/";
        }).fail(function(error) {
            alert("Failed Delete Post");
        })
    },

    //댓글 저장
    saveReply: function() {
        let data = {
            //댓글 작성 ID, 게시글 ID, 댓글 내용
            userId: $("#userId").val(),
            boardId: $("#boardId").val(),
            content: $("#reply-content").val()
        }
        $.ajax({
            type: "POST",
            url: `/api/board/${data.boardId}/reply`,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
        }).done(function (resp) {
            alert("Success Save Reply");
            location.href = `/board/${data.boardId}`;
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    },

    //댓글 삭제 - 게시글 Id와 댓글 Id를 매개변수로
    deleteReply: function (boardId,replyId) {
        $.ajax({
            type: "DELETE",
            url: `/api/board/reply/${replyId}`,
            contentType: "application/json; charset=utf-8",
        }).done(function (resp) {
            alert("Success Remove Reply");
            location.href = `/board/${boardId}`;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
}
//js 최초 로딩 시 init 메소드 호출하여 각 버튼의 맵핑에 맞게
index.init();