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
    }
}

//js 최초 로딩 시 init 메소드 호출하여 각 버튼의 맵핑에 맞게
index.init();