package syim.reviewboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequestDto {
    private String title;
    private String content;
    private String category;
    private BookDto book;
}
