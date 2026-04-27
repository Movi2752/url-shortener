package shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class LinkStatsResponse {
    private String code;
    private String originalUrl;
    private Long clicks;
    private Instant createdAt;
}