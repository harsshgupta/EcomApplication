package com.project.ecomapplication.services;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.project.ecomapplication.entities.AccessToken;
import com.project.ecomapplication.entities.User;
import com.project.ecomapplication.exceptions.ObjectNotFoundException;
import com.project.ecomapplication.exceptions.TokenRefreshException;
import com.project.ecomapplication.entities.RefreshToken;
import com.project.ecomapplication.repository.RefreshTokenRepository;
import com.project.ecomapplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class
RefreshTokenService {

    @Value("${harsh.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        RefreshToken newToken = refreshTokenRepository.save(refreshToken);
        return newToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new sign-in request");
        }
        return token;
    }

}