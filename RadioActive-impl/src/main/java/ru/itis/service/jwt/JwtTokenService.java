package ru.itis.service.jwt;

import ru.itis.dto.TokenCoupleDto;
import ru.itis.dto.response.TokenCoupleResponse;
import ru.itis.dto.response.UserResponse;

public interface JwtTokenService {
    UserResponse getUserInfoByToken(String token);

    TokenCoupleResponse generateTokenCouple(UserResponse userResponse);

    TokenCoupleResponse refreshAccessToken(TokenCoupleDto tokenCoupleDto);
}
