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
public class TheaterDto {
    private String title;

    private String genre;

    private String startDate;

    private String endDate;

    private String imageURL;

    private String place;
    @Column(unique = true, nullable = false)
    private String mt20id;

}
