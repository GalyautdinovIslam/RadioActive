package ru.itis.services;

import ru.itis.dto.request.UserExtendedRequest;
import ru.itis.dto.response.UserResponse;

import java.util.UUID;

public interface UserService {
    UUID createUser(UserExtendedRequest userExtendedRequest);

    UserResponse getUser(UUID userId);

    UserResponse updateUser(UUID userId, UserExtendedRequest userExtendedRequest);

    void deleteUser(UUID userId);

    UserResponse addRoomToFavorite(UUID userId, UUID roomId);

    UserResponse deleteRoomFromFavorite(UUID userId, UUID roomId);
}
