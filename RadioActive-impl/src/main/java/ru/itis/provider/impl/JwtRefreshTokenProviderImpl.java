package ru.itis.provider.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.dto.response.UserResponse;
import ru.itis.models.RefreshTokenEntity;
import ru.itis.provider.AccountProvider;
import ru.itis.provider.JwtRefreshTokenProvider;

@RequiredArgsConstructor
@Component
public class JwtRefreshTokenProviderImpl implements JwtRefreshTokenProvider {
    private final AccountProvider accountProvider;

    @Override
    public String generateRefreshToken(UserResponse userResponse) {
        return accountProvider.getUserRefreshTokenService().generateRefreshToken(userResponse).getId().toString();
    }

    @Override
    public RefreshTokenEntity verifyRefreshTokenExpiration(String accessToken) {
        return accountProvider.getUserRefreshTokenService().verifyRefreshTokenExpiryDate(accessToken);
    }
}
