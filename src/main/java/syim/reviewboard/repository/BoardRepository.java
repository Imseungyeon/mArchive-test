package syim.reviewboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import syim.reviewboard.model.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    //Long은 id, Board는 select/update/save 메소드들에 대한 동작 리턴 시 Board로 맵핑하여 리턴
}
