package ru.itis.service.jwt.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.itis.dto.TokenCoupleDto;
import ru.itis.dto.response.TokenCoupleResponse;
import ru.itis.dto.response.UserResponse;
import ru.itis.model.RefreshTokenEntity;
import ru.itis.provider.JwtRefreshTokenProvider;
import ru.itis.provider.JwtTokenProvider;
import ru.itis.service.jwt.JwtTokenService;

import java.util.HashMap;

import static ru.itis.constant.RadioActiveConstants.BEARER;

@RequiredArgsConstructor
@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtTokenProvider jwtAccessTokenProvider;
    private final JwtRefreshTokenProvider jwtRefreshTokenProvider;

    @Override
    public UserResponse getUserInfoByToken(String token) {
        return jwtAccessTokenProvider.userInfoByToken(token);
    }

    @Override
    public TokenCoupleResponse generateTokenCouple(UserResponse userResponse) {
        String token = jwtAccessTokenProvider.generateAccessToken(
                userResponse.getNickname(),
                new HashMap<>()
        );
        String refreshToken = jwtRefreshTokenProvider.generateRefreshToken(userResponse);
        return TokenCoupleResponse.builder()
                .accessToken(BEARER.concat(token))
                .refreshToken(refreshToken)
                .accessTokenExpirationDate(jwtAccessTokenProvider.getExpirationDateFromAccessToken(token))
                .build();
    }

    @Override
    public TokenCoupleResponse refreshAccessToken(TokenCoupleDto tokenCoupleDto) {
        RefreshTokenEntity verifiedRefreshToken = jwtRefreshTokenProvider.verifyRefreshTokenExpiration(
                tokenCoupleDto.getRefreshToken()
        );
        String accessToken = jwtAccessTokenProvider.generateAccessToken(
                jwtAccessTokenProvider.getSubjectFromAccessToken(tokenCoupleDto.getAccessToken()
                        .replace(BEARER, StringUtils.EMPTY)), new HashMap<>()
        );
        return TokenCoupleResponse.builder()
                .refreshToken(String.valueOf(verifiedRefreshToken.getId()))
                .accessToken(BEARER.concat(accessToken))
                .accessTokenExpirationDate(jwtAccessTokenProvider.getExpirationDateFromAccessToken(accessToken))
                .build();
    }
}
