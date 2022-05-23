package ru.itis.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.api.UserApi;
import ru.itis.dto.request.UserExtendedRequest;
import ru.itis.dto.response.UserResponse;
import ru.itis.services.UserService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<UUID> createUser(UserExtendedRequest userExtendedRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userExtendedRequest));
    }

    @Override
    public ResponseEntity<UserResponse> getUser(UUID userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(UUID userId, UserExtendedRequest userExtendedRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userService.updateUser(userId, userExtendedRequest));
    }

    @Override
    public ResponseEntity<?> deleteUser(UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public ResponseEntity<UserResponse> addRoomToFavorite(UUID userId, UUID roomId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addRoomToFavorite(userId, roomId));
    }

    @Override
    public ResponseEntity<UserResponse> deleteRoomFromFavorite(UUID userId, UUID roomId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(userService.deleteRoomFromFavorite(userId, roomId));
    }
}
