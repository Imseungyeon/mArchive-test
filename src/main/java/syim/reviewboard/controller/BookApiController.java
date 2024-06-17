package syim.reviewboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import syim.reviewboard.dto.BookDto;
import syim.reviewboard.model.Book;
import syim.reviewboard.service.BookService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book/api")
public class BookApiController {
    private final BookService bookService;

    @Autowired
    public BookApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/search")
    public List<Book> apiSearch(@RequestBody Map<String, String> request) {
        String keyword = request.get("keyword");
        return bookService.getBookFromApi(keyword);
    }
}
