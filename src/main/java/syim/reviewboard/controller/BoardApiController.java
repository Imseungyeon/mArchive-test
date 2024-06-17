package syim.reviewboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import syim.reviewboard.config.auth.PrincipalDetail;
import syim.reviewboard.dto.BoardRequestDto;
import syim.reviewboard.dto.BookDto;
import syim.reviewboard.dto.ReplySaveRequestDto;
import syim.reviewboard.dto.ResponseDto;
import syim.reviewboard.model.Book;
import syim.reviewboard.service.BoardService;
import syim.reviewboard.model.Board;
import syim.reviewboard.service.BookService;

@RestController
public class BoardApiController {
    @Autowired
    private BoardService boardService;

    @Autowired
    private BookService bookService;

    @PostMapping("/api/board")
    public ResponseDto<Integer> save(@RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal PrincipalDetail principalDetail){
        Board board = new Board();
        board.setTitle(boardRequestDto.getTitle());
        board.setContent(boardRequestDto.getContent());
        board.setCategory(boardRequestDto.getCategory());

        BookDto bookDto = boardRequestDto.getBook();

        boardService.writePost(board, principalDetail.getUser(), bookDto);
        return new ResponseDto<>(HttpStatus.OK, 1);
    }

    @DeleteMapping("/api/board/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable int id){
        boardService.deletePost(id);
        return new ResponseDto<>(HttpStatus.OK, 1);
    }

    @PutMapping("/api/board/{id}")
    public ResponseDto<Integer> update(@PathVariable int id, @RequestBody Board board){
        boardService.updatePost(id, board);
        return new ResponseDto<>(HttpStatus.OK, 1);
    }

    @PostMapping("/api/board/{boardId}/reply")
    public ResponseDto<Integer> saveReply(@RequestBody ReplySaveRequestDto reply){
        boardService.writeReply(reply);
        return new ResponseDto<>(HttpStatus.OK, 1);
    }

    @DeleteMapping("/api/board/reply/{replyId}")
    public ResponseDto<Integer> deleteReply(@PathVariable long replyId) {
        boardService.deleteReply(replyId);
        return new ResponseDto<>(HttpStatus.OK, 1);
    }
}
