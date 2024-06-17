package syim.reviewboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import syim.reviewboard.dto.BookDto;
import syim.reviewboard.model.Book;
import syim.reviewboard.service.BookService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping("/search")
    public String BookSearch(Model model) {
        model.addAttribute("keyword", "");
        return "board/bookSearch";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute("keyword") String keyword, Model model) {
        List<Book> bookList = bookService.getBookFromApi(keyword);
        model.addAttribute("bookList", bookList);
        return "board/bookSearchResult";
    }

}
