package ru.itis.api.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.TokenCoupleDto;
import ru.itis.dto.response.TokenCoupleResponse;
import ru.itis.dto.response.UserResponse;

@RequestMapping("/api/v1/token")
public interface JwtTokenApi {

    @GetMapping(value = "/user-info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    UserResponse userInfoByToken(@RequestParam String token);

    @PostMapping(value = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TokenCoupleResponse updateTokens(@RequestBody TokenCoupleDto tokenCoupleDto);
}
