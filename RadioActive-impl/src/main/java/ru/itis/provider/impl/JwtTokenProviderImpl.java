package ru.itis.provider.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itis.dto.response.UserResponse;
import ru.itis.exceptions.AuthenticationHeaderException;
import ru.itis.provider.AccountProvider;
import ru.itis.provider.JwtTokenProvider;
import ru.itis.services.UserService;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private final AccountProvider accountProvider;

    @Value("${jwt.expiration.access.millis}")
    private long expirationAccessInMillis;

    @Value("${jwt.secret}")
    private String jwtSecret;


    @Override
    public String generateAccessToken(String subject, Map<String, Object> data) {
        Map<String, Object> claims = new HashMap<>(data);
        claims.put(Claims.SUBJECT, subject);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(Instant.now().plusMillis(expirationAccessInMillis)))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    @Override
    public boolean validateAccessToken(String accessToken, String subject) {
        try {
            Claims claims = parseAccessToken(accessToken);
            String subjectFromToken = claims.getSubject();
            Date date = claims.getExpiration();
            return subject.equals(subjectFromToken) && date.after(date);
        } catch (ExpiredJwtException exception) {
            throw new AuthenticationHeaderException("Token expired date error");
        }
    }

    @Override
    public UserResponse userInfoByToken(String token) {
        try {
            Claims claims = parseAccessToken(token);
            String subject = claims.getSubject();
            UserService userService = accountProvider.getUserService();
            return userService.findBySubject(subject)
                    .orElseThrow(() -> new AuthenticationHeaderException("User with this name was not found"));
        } catch (ExpiredJwtException e) {
            throw new AuthenticationHeaderException("Token expired date error");
        }

    }

    @Override
    public Claims parseAccessToken(String accessToken) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(accessToken)
                .getBody();
    }

    @Override
    public Date getExpirationDateFromAccessToken(String accessToken) {
        try {
            return parseAccessToken(accessToken).getExpiration();
        } catch (ExpiredJwtException exception) {
            return exception.getClaims().getExpiration();
        }
    }

    @Override
    public String getSubjectFromAccessToken(String accessToken) {
        try {
            return parseAccessToken(accessToken).getSubject();
        } catch (ExpiredJwtException exception) {
            return exception.getClaims().getSubject();
        }
    }
}
