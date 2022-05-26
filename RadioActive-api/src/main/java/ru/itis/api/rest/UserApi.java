package ru.itis.api.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.request.UserExtendedRequest;
import ru.itis.dto.response.UserResponse;

import java.util.UUID;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RequestMapping("api/v1/users")
public interface UserApi {

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<UUID> createUser(@RequestBody UserExtendedRequest userExtendedRequest);

    @GetMapping(value = "/{user-id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<UserResponse> getUser(@PathVariable("user-id") UUID userId);

    @PutMapping(value = "/{user-id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<UserResponse> updateUser(@PathVariable("user-id") UUID userId,
                                            @RequestBody UserExtendedRequest userExtendedRequest);

    @DeleteMapping(value = "/{user-id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<?> deleteUser(@PathVariable("user-id") UUID userId);

    @PostMapping(value = "/favorites", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<UserResponse> addRoomToFavorite(UUID userId, UUID roomId);

    @DeleteMapping(value = "/favorites", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<UserResponse> deleteRoomFromFavorite(UUID userId, UUID roomId);
}
