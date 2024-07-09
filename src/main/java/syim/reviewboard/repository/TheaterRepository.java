package syim.reviewboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import syim.reviewboard.model.Theater;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    Theater findByMt20id(String mt20id);
}
