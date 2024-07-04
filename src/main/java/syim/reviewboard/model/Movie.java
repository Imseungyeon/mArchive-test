package syim.reviewboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql의 AUTO_INCREMENT
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String englishTitle;
    @Column(nullable = false)
    private String productionYear;
    @Column(nullable = false)
    private String director;
    @Column(nullable = false)
    private String genre;
    @Column(nullable = false)
    private String nation;
    @Column(unique = true, nullable = false)
    private String movieId; //API에서 받은 고유 영화 Id

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards;
}
