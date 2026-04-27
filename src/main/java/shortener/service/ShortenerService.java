package shortener.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shortener.entity.Link;
import shortener.repository.LinkRepository;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class ShortenerService {

    private static final String ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;
    private static final int MAX_ATTEMPTS = 5;

    private final LinkRepository linkRepository;
    private final SecureRandom random = new SecureRandom();

    @Value("${app.base-url}")
    private String baseUrl;

    @Transactional
    public Link shorten(String originalUrl) {
        String code = generateUniqueCode();
        Link link = Link.builder()
                .code(code)
                .originalUrl(originalUrl)
                .clicks(0L)
                .build();
        return linkRepository.save(link);
    }

    public String buildShortUrl(String code) {
        return baseUrl + "/" + code;
    }

    private String generateUniqueCode() {
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            String candidate = randomCode();
            if (!linkRepository.existsByCode(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException(
                "Failed to generate unique code after " + MAX_ATTEMPTS + " attempts");
    }

    private String randomCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}