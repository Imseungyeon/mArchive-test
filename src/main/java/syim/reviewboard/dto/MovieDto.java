package syim.reviewboard.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto {
//    private Long id;
    private String title;
    private String englishTitle;
    private String productionYear;
    private String director;
    private String genre;
    private String nation;

    @Column(unique = true, nullable = false)
    private String movieId;

//    @Builder
//    public MovieDto(String title, String englishTitle, String productionYear, String director, String genre, String nation, String movieId){
//        this.title = title;
//        this.englishTitle = englishTitle;
//        this.productionYear = productionYear;
//        this.director = director;
//        this.genre = genre;
//        this.nation = nation;
//        this.movieId = movieId;
//    }
}
