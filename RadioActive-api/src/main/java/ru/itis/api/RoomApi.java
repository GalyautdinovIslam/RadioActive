package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.request.RoomExtendedRequest;
import ru.itis.dto.request.RoomRequest;
import ru.itis.dto.response.RoomResponse;

import java.util.UUID;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RequestMapping("/api/v1/rooms")
public interface RoomApi {

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<UUID> createRoom(@RequestBody RoomRequest roomRequest);

    @GetMapping(value = "/{room-id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<RoomResponse> getRoom(@PathVariable("room-id") UUID roomId);

    @PutMapping(value = "/{room-id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<RoomResponse> updateRoom(@PathVariable("room-id") UUID roomId,
                                            @RequestBody RoomExtendedRequest roomExtendedRequest);

    @DeleteMapping(value = "/{room-id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<?> deleteRoom(@PathVariable("room-id") UUID roomId);

    @PostMapping(value = "/admins", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<RoomResponse> addAdmin(UUID roomId, UUID userId);

    @DeleteMapping(value = "/admins", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<RoomResponse> deleteAdmin(UUID roomId, UUID userId);

    @PostMapping(value = "/bans", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<RoomResponse> banUser(UUID roomId, UUID userId);

    @DeleteMapping(value = "/bans", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<RoomResponse> unbanUser(UUID roomId, UUID userId);
}
