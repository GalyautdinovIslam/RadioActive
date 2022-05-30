package ru.itis.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import ru.itis.security.filter.TokenAuthenticationFilter;
import ru.itis.security.userdetails.TokenAuthenticationUserDetailsService;
import ru.itis.services.jwt.JwtTokenService;

import java.util.Collections;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenAuthenticationUserDetailsService authenticationUserDetailsService;
    private final JwtTokenService jwtTokenService;

    private static final String[] PERMIT_ALL = {
            "/api/v1/users/login",
            "/api/v1/token/user-info",
            "/api/v1/token/refresh"
    };

    private static final String[] IGNORE = {
            "/account-swagger/api-docs",
            "/swagger-ui.html"
    };

    @Override
    protected AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(IGNORE);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAfter(tokenAuthorizationFilter(), RequestHeaderAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider())
                .authorizeRequests()
                .antMatchers(PERMIT_ALL).permitAll()
                .and()
                .cors()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(authenticationUserDetailsService);
        authenticationProvider.setThrowExceptionWhenTokenRejected(false);
        return authenticationProvider;
    }

    public RequestHeaderAuthenticationFilter tokenAuthorizationFilter() {
        return new TokenAuthenticationFilter(jwtTokenService, authenticationManager());
    }
}
