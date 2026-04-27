package shortener.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import shortener.dto.ShortenRequest;
import shortener.dto.ShortenResponse;
import shortener.entity.Link;
import shortener.service.ShortenerService;

@RestController
@RequiredArgsConstructor
public class ShortenerController {

    private final ShortenerService shortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> shorten(@Valid @RequestBody ShortenRequest request) {
        Link link = shortenerService.shorten(request.getUrl());
        ShortenResponse response = new ShortenResponse(
                link.getCode(),
                shortenerService.buildShortUrl(link.getCode())
        );
        return ResponseEntity.ok(response);
    }
}