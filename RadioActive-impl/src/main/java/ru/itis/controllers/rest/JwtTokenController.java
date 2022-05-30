package ru.itis.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.api.rest.JwtTokenApi;
import ru.itis.dto.TokenCoupleDto;
import ru.itis.dto.response.TokenCoupleResponse;
import ru.itis.dto.response.UserResponse;
import ru.itis.services.jwt.JwtTokenService;

@RequiredArgsConstructor
@RestController
public class JwtTokenController implements JwtTokenApi {

    private final JwtTokenService jwtTokenService;

    @Override
    public UserResponse userInfoByToken(String token) {
        return jwtTokenService.getUserInfoByToken(token);
    }

    @Override
    public TokenCoupleResponse updateTokens(TokenCoupleDto tokenCoupleDto) {
        return jwtTokenService.refreshAccessToken(tokenCoupleDto);
    }
}
