package ru.itis.provider;

import ru.itis.dto.response.UserResponse;
import ru.itis.model.RefreshTokenEntity;

public interface JwtRefreshTokenProvider {
    String generateRefreshToken(UserResponse userResponse);

    RefreshTokenEntity verifyRefreshTokenExpiration(String accessToken);
}
