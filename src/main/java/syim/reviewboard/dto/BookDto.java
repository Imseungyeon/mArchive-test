package syim.reviewboard.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String imageURL;
    //private String publisher;
    //private String description;
    @Column(unique = true, nullable = false)
    private String apiId;
    @Builder
    public BookDto(String title, String author, String imageURL, String apiId) {
        this.title = title;
        this.author = author;
        this.imageURL = imageURL;
        this.apiId = apiId;
    }
}
