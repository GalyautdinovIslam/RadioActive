package ru.itis.provider;

import io.jsonwebtoken.Claims;
import ru.itis.dto.response.UserResponse;

import java.util.Date;
import java.util.Map;

public interface JwtTokenProvider {
    String generateAccessToken(String subject, Map<String, Object> data);

    boolean validateAccessToken(String accessToken, String subject);

    UserResponse userInfoByToken(String token);

    Claims parseAccessToken(String accessToken);

    Date getExpirationDateFromAccessToken(String accessToken);

    String getSubjectFromAccessToken(String accessToken);
}
