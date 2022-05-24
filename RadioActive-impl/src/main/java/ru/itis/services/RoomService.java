package ru.itis.services;

import ru.itis.dto.request.RoomExtendedRequest;
import ru.itis.dto.request.RoomRequest;
import ru.itis.dto.response.RoomResponse;

import java.util.UUID;

public interface RoomService {
    UUID createRoom(RoomRequest roomRequest);

    RoomResponse getRoom(UUID roomId);

    RoomResponse updateRoom(UUID roomId, RoomExtendedRequest roomExtendedRequest);

    void deleteRoom(UUID roomId);

    RoomResponse addAdmin(UUID roomId, UUID userId);

    RoomResponse deleteAdmin(UUID roomId, UUID userId);

    RoomResponse banUser(UUID roomId, UUID userId);

    RoomResponse unbanUser(UUID roomId, UUID userId);
}
