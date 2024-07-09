package syim.reviewboard.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import syim.reviewboard.model.Theater;
import syim.reviewboard.service.TheaterService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/theater/api")
public class TheaterApiController {
    private final TheaterService theaterService;
    public TheaterApiController(TheaterService theaterService){this.theaterService = theaterService;}

    @PostMapping("/search")
    public List<Theater> apiSearch(@RequestBody Map<String, String> request) {
        String keyword = request.get("keyword");
        return theaterService.getTheaterFromApi(keyword);
    }
}
