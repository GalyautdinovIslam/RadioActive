package ru.itis.provider;

import ru.itis.service.UserService;
import ru.itis.service.jwt.AccountRefreshTokenService;

public interface AccountProvider {
    UserService getUserService();

    AccountRefreshTokenService getUserRefreshTokenService();
}
