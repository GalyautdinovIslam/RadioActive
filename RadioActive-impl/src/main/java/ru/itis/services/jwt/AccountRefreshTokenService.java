package ru.itis.services.jwt;

import ru.itis.dto.response.UserResponse;
import ru.itis.models.RefreshTokenEntity;

public interface AccountRefreshTokenService {
    RefreshTokenEntity generateRefreshToken(UserResponse userResponse);
    RefreshTokenEntity verifyRefreshTokenExpiryDate(String refreshToken);
}
