package syim.reviewboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import syim.reviewboard.model.Theater;
import syim.reviewboard.service.TheaterService;

import java.util.List;

@Controller
@RequestMapping("/Theater")
public class TheaterController {
    private TheaterService theaterService;
    public TheaterController(TheaterService theaterService){this.theaterService = theaterService;}
    @GetMapping("/search")
    public String TheaterSearch(Model model) {
        model.addAttribute("keyword", "");
        return "board/theaterSearch";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute("keyword") String keyword, Model model) {
        List<Theater> theaterList = theaterService.getTheaterFromApi(keyword);
        model.addAttribute("theaterList", theaterList);
        return "board/theaterSearchResult";
    }
}
