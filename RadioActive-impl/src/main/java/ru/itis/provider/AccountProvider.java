package ru.itis.provider;

import ru.itis.services.UserService;
import ru.itis.services.jwt.AccountRefreshTokenService;
import ru.itis.services.jwt.JwtTokenService;

public interface AccountProvider {
    UserService getUserService();

    AccountRefreshTokenService getUserRefreshTokenService();
}
