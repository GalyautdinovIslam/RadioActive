package ru.itis.provider.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.provider.AccountProvider;
import ru.itis.services.UserService;
import ru.itis.services.jwt.AccountRefreshTokenService;

@RequiredArgsConstructor
@Component
public class AccountProviderImpl implements AccountProvider {

    private final UserService userService;
    private final AccountRefreshTokenService userRefreshTokenService;

    @Override
    public UserService getUserService() {
        return userService;
    }

    @Override
    public AccountRefreshTokenService getUserRefreshTokenService() {
        return userRefreshTokenService;
    }
}
