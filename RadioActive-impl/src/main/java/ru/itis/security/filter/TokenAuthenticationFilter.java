package ru.itis.security.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import ru.itis.dto.response.UserResponse;
import ru.itis.exception.AuthenticationHeaderException;
import ru.itis.service.jwt.JwtTokenService;
import ru.itis.util.HttpRequestUtil;
import ru.itis.util.HttpResponseUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static ru.itis.constant.RadioActiveConstants.BEARER;

public class TokenAuthenticationFilter extends RequestHeaderAuthenticationFilter {

    private final JwtTokenService jwtTokenService;

    public TokenAuthenticationFilter(JwtTokenService jwtTokenService, AuthenticationManager authenticationManager) {
        this.jwtTokenService = jwtTokenService;
        this.setAuthenticationManager(authenticationManager);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        try {
            String token = getTokenFromValidatedAuthorizationHeader(((HttpServletRequest) request)
                    .getHeader(AUTHORIZATION));
            if (Objects.nonNull(token)) {
                UserResponse user = jwtTokenService.getUserInfoByToken(token);
                PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(user,
                        token);

                if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())
                        || !SecurityContextHolder.getContext().getAuthentication().getCredentials().equals(token)) {
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            chain.doFilter(request, response);
        } catch (Exception exception) {
            SecurityContextHolder.clearContext();
            HttpResponseUtil.putExceptionInResponse((HttpServletRequest) request, (HttpServletResponse) response,
                    exception, HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private String getTokenFromValidatedAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null) return null;
        if (!authorizationHeader.startsWith(BEARER))
            throw new AuthenticationHeaderException("Invalid authentication scheme found in Authorization header");
        String token = HttpRequestUtil.getTokenFromAuthorizationHeader(authorizationHeader);
        if (token == null)
            throw new AuthenticationHeaderException("Authorization header token is empty");
        return token;
    }
}
