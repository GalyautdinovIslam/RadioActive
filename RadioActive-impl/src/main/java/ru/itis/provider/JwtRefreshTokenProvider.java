package ru.itis.provider;

import ru.itis.dto.response.UserResponse;
import ru.itis.models.RefreshTokenEntity;

public interface JwtRefreshTokenProvider {
    String generateRefreshToken(UserResponse userResponse);

    RefreshTokenEntity verifyRefreshTokenExpiration(String accessToken);
}
