package ru.itis.service.jwt;

import ru.itis.dto.response.UserResponse;
import ru.itis.model.RefreshTokenEntity;

public interface AccountRefreshTokenService {
    RefreshTokenEntity generateRefreshToken(UserResponse userResponse);

    RefreshTokenEntity verifyRefreshTokenExpiryDate(String refreshToken);
}
