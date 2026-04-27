package shortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShortenRequest {

    @NotBlank(message = "URL must not be empty")
    @Size(max = 2048, message = "URL is too long")
    private String url;
}