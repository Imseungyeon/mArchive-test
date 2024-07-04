package syim.reviewboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import syim.reviewboard.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findByMovieId(String apiId);
}
