package shortener.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shortener.dto.ShortenRequest;
import shortener.dto.ShortenResponse;
import shortener.entity.Link;
import shortener.service.ShortenerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.net.URI;
import shortener.dto.LinkStatsResponse;

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

    @GetMapping("/{code:[A-Za-z0-9]{6}}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        Link link = shortenerService.resolveAndIncrementClicks(code);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(link.getOriginalUrl()));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/stats/{code:[A-Za-z0-9]{6}}")
    public ResponseEntity<LinkStatsResponse> stats(@PathVariable String code) {
        Link link = shortenerService.getByCode(code);
        LinkStatsResponse response = new LinkStatsResponse(
                link.getCode(),
                link.getOriginalUrl(),
                link.getClicks(),
                link.getCreatedAt()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{code:[A-Za-z0-9]{6}}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        shortenerService.deleteByCode(code);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/links")
    public ResponseEntity<java.util.List<LinkStatsResponse>> list() {
        var all = shortenerService.listAll();
        var out = all.stream().map(l -> new LinkStatsResponse(
                l.getCode(), l.getOriginalUrl(), l.getClicks(), l.getCreatedAt()
        )).toList();
        return ResponseEntity.ok(out);
    }
}