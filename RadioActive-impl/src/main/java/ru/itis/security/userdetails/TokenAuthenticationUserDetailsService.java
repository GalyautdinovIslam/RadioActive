package ru.itis.security.userdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.itis.dto.response.UserResponse;
import ru.itis.exceptions.AuthenticationHeaderException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenAuthenticationUserDetailsService implements
        AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        return loadUserDetails((UserResponse) token.getPrincipal(), String.valueOf(token.getCredentials()));
    }

    private UserDetails loadUserDetails(UserResponse userResponse, String token) {
        try {
            return Optional.ofNullable(userResponse)
                    .map(account -> UserAccount.builder()
                            .id(account.getId())
                            .nickname(account.getNickname())
                            .createDate(null)
                            .isAccountNonExpired(true)
                            .isCredentialsNonExpired(true)
                            .isAccountNonLocked(true)
                            .isEnabled(true)
                            .token(token)
                            .build())
                    .orElseThrow(() -> new UsernameNotFoundException("Unknown user by token " + token));
        } catch (Exception exception) {
            throw new AuthenticationHeaderException(exception.getMessage());
        }
    }
}
