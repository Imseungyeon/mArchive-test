package syim.reviewboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto<T> {
    //controller에서 응담을 보낼 때 Httpstatus와 데이터를 맵핑하여 리턴
    HttpStatus status;
    T data;
}
