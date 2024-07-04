package syim.reviewboard.service;

import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import syim.reviewboard.dto.BookDto;
import syim.reviewboard.dto.MovieDto;
import syim.reviewboard.dto.ReplySaveRequestDto;
import syim.reviewboard.model.*;
import syim.reviewboard.repository.*;


@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ReplyRepository replyRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    //게시글을 저장
    @Transactional
    public void writePost(Board board, User user, BookDto bookDto, MovieDto movieDto) {
        board.setUser(user); // 실제로 저장될 때는 user에 해당하는 id만 저장됨
        if (bookDto != null) {
            Book book = bookRepository.findByApiId(bookDto.getApiId());
            if (book == null) {
                book = convertToBook(bookDto);
                bookRepository.save(book);
            }
            board.setBook(book);
        }

        if (movieDto != null) {
            Movie movie = movieRepository.findByMovieId(movieDto.getMovieId());
            if (movie == null) {
                movie = convertToMovie(movieDto);
                movieRepository.save(movie);
            }
            board.setMovie(movie);
        }
        boardRepository.save(board);

    }

    private Book convertToBook(BookDto bookDto) {
        Book book = new Book();

        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setImageURL(bookDto.getImageURL());
        book.setApiId(bookDto.getApiId());

        return book;
    }

    private Movie convertToMovie(MovieDto movieDto) {
        Movie movie = new Movie();

        movie.setTitle(movieDto.getTitle());
        movie.setEnglishTitle(movieDto.getEnglishTitle());
        movie.setProductionYear(movieDto.getProductionYear());
        movie.setDirector(movieDto.getDirector());
        movie.setGenre(movieDto.getGenre());
        movie.setNation(movieDto.getNation());
        movie.setMovieId(movieDto.getMovieId());

        return movie;
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
        board.setCategory(requestBoard.getCategory());

        // 책 정보는 그대로 유지
        if (requestBoard.getBook() != null) {
            Book book = bookRepository.findByApiId(requestBoard.getBook().getApiId());
            if (book == null) {
                book = requestBoard.getBook();
                bookRepository.save(book);
            }
            board.setBook(book);
        }
        boardRepository.save(board);
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
