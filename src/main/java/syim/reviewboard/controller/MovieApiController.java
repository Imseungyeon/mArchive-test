package syim.reviewboard.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import syim.reviewboard.model.Movie;
import syim.reviewboard.service.MovieService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movie/api")
public class MovieApiController {
    private final MovieService movieService;

    public MovieApiController(MovieService movieService) { this.movieService = movieService; }

    @PostMapping("/search")
    public List<Movie> apiSearch(@RequestBody Map<String, String> request) {
        String keyword = request.get("keyword");
        return movieService.getMovieFromApi(keyword);
    }
}
