package syim.reviewboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import syim.reviewboard.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByApiId(String apiId);
}
