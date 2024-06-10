package syim.reviewboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import syim.reviewboard.dto.ReplySaveRequestDto;
import syim.reviewboard.model.Board;
import syim.reviewboard.model.Reply;
import syim.reviewboard.model.User;
import syim.reviewboard.repository.BoardRepository;
import syim.reviewboard.repository.ReplyRepository;
import syim.reviewboard.repository.UserRepository;


@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReplyRepository replyRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    //게시글을 저장
    @Transactional
    public void writePost(Board board, User user) {
        board.setUser(user); // 실제로 저장될 때는 user에 해당하는 id만 저장됨
        boardRepository.save(board);
    }

    //게시글 리스트 출력
    public Page<Board> getPostList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    //특정 게시글을 출력 - 특정 id를 key로 삼아서 select
    @Transactional(readOnly = true)
    public Board getPost(long id) {
        //Board객체가 없으면 Exception 발생
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Failed to load post : cannot find post id"));
    }

    //게시글을 삭제
    @Transactional
    public void deletePost(long id) {
        boardRepository.deleteById(id);
    }

    //게시글을 수정
    @Transactional
    public void updatePost(long id, Board requestBoard) {
        // id로 데이터베이스의 정보를 가져오고 ui에서 수정된 정보를 가져와 맵핑
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Failed to load post : cannot find post id"));
        board.setTitle(requestBoard.getTitle());
        board.setContent(requestBoard.getContent());
    }

    //댓글 작성
    @Transactional
    public void writeReply(ReplySaveRequestDto replyDto) {
        //Dto에 담긴 아이디로 실제 게시판 아이디 확인
        Board board = boardRepository.findById(replyDto.getBoardId()).orElseThrow(() -> new IllegalArgumentException("Failed to write reply : cannot find ;ost id"));
        //실제 있는 사용자인지 확인
        User user = userRepository.findById(replyDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("Failed to write reply :  cannot find post id"));
        //객체 생성
        Reply reply = Reply.builder()
                .user(user)
                .board(board)
                .content(replyDto.getContent()).build();

        replyRepository.save(reply);
    }

    //댓글 삭제
    @Transactional
    public void deleteReply(long replyId) {
        //댓글의 아이디를 통해 해당 row 삭제
        replyRepository.deleteById(replyId);
    }
}
