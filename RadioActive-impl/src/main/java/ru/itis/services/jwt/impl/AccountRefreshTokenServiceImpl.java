package ru.itis.services.jwt.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.dto.response.UserResponse;
import ru.itis.exceptions.TokenRefreshException;
import ru.itis.exceptions.UserNotFoundException;
import ru.itis.models.RefreshTokenEntity;
import ru.itis.models.UserRefreshTokenEntity;
import ru.itis.repositories.UserRefreshTokenRepository;
import ru.itis.repositories.UserRepository;
import ru.itis.services.jwt.AccountRefreshTokenService;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AccountRefreshTokenServiceImpl implements AccountRefreshTokenService {

    @Value("${jwt.expiration.refresh.millis}")
    private long expirationRefreshInMills;

    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RefreshTokenEntity generateRefreshToken(UserResponse userResponse) {
        return userRefreshTokenRepository.save(
                UserRefreshTokenEntity.builder()
                        .expiryDate(Instant.now().plusMillis(expirationRefreshInMills))
                        .account(userRepository
                                .findOneByNickname(userResponse.getNickname())
                                .orElseThrow(UserNotFoundException::new))
                        .build()
        );
    }

    @Override
    public RefreshTokenEntity verifyRefreshTokenExpiryDate(String refreshToken) {
        return userRefreshTokenRepository.findById(UUID.fromString(refreshToken))
                .map(token -> {
                    userRefreshTokenRepository.delete(token);
                    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
                        throw new TokenRefreshException(String.valueOf(token.getId()), "Refresh token expired");
                    }
                    return userRefreshTokenRepository.save(
                            UserRefreshTokenEntity.builder()
                                    .expiryDate(Instant.now().plusMillis(expirationRefreshInMills))
                                    .account(token.getAccount())
                                    .build());
                }).orElseThrow(() -> new TokenRefreshException(refreshToken, "Token does not exist"));
    }
}
