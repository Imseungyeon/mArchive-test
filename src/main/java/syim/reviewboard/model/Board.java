package syim.reviewboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String content;

    @Column(nullable = false, length = 20)
    private String category;

    //하나의 게시글은 한 유저에게만, 한 유저는 여러개의 게시글 작성 가능(Many = Board, One = User)
    //패치 시점(객체를 언제 로딩하는지) 로딩 시점 설정 - lazy loading 전략은 one에 해당하는 객체가 필요한 시점에 다시 객체를 날려 초기화
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId") //user에 userID값이 같이 저장됨
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    // 주인 아닐 시 읽기만 가능하도록
    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id desc")
    private List<Reply> replys;

    @CreationTimestamp
    private Timestamp createDate;
}
