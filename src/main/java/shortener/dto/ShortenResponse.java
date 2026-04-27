package shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortenResponse {
    private String code;
    private String shortUrl;
}