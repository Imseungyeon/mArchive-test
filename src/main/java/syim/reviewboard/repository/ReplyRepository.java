package syim.reviewboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import syim.reviewboard.model.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
