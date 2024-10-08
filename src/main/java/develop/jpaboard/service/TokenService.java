package develop.jpaboard.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import develop.jpaboard.config.jwt.TokenProvider;
import develop.jpaboard.domain.User;

import java.time.Duration;
@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        if(!tokenProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
