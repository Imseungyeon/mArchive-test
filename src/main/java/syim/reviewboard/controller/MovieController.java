package syim.reviewboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import syim.reviewboard.model.Movie;
import syim.reviewboard.service.MovieService;

import java.util.List;

@Controller
@RequestMapping("/movie")
public class MovieController {
    private MovieService movieService;

    public MovieController(MovieService movieService){ this.movieService = movieService; }

    @GetMapping("/search")
    public String MovieSearch(Model model) {
        model.addAttribute("keyword", "");
        return "board/movieSearch";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute("keyword") String keyword, Model model) {
        List<Movie> movieList = movieService.getMovieFromApi(keyword);
        model.addAttribute("movieList", movieList);
        return "board/movieSearchResult";
    }
}
