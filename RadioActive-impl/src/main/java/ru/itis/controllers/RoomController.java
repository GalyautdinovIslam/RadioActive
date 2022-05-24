package ru.itis.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.api.RoomApi;
import ru.itis.dto.request.RoomExtendedRequest;
import ru.itis.dto.request.RoomRequest;
import ru.itis.dto.response.RoomResponse;
import ru.itis.services.RoomService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class RoomController implements RoomApi {

    private final RoomService roomService;

    @Override
    public ResponseEntity<UUID> createRoom(RoomRequest roomRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomService.createRoom(roomRequest));
    }

    @Override
    public ResponseEntity<RoomResponse> getRoom(UUID roomId) {
        return ResponseEntity.ok(roomService.getRoom(roomId));
    }

    @Override
    public ResponseEntity<RoomResponse> updateRoom(UUID roomId, RoomExtendedRequest roomExtendedRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(roomService.updateRoom(roomId, roomExtendedRequest));
    }

    @Override
    public ResponseEntity<?> deleteRoom(UUID roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public ResponseEntity<RoomResponse> addAdmin(UUID roomId, UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomService.addAdmin(roomId, userId));
    }

    @Override
    public ResponseEntity<RoomResponse> deleteAdmin(UUID roomId, UUID userId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(roomService.deleteAdmin(roomId, userId));
    }

    @Override
    public ResponseEntity<RoomResponse> banUser(UUID roomId, UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomService.banUser(roomId, userId));
    }

    @Override
    public ResponseEntity<RoomResponse> unbanUser(UUID roomId, UUID userId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(roomService.unbanUser(roomId, userId));
    }
}
