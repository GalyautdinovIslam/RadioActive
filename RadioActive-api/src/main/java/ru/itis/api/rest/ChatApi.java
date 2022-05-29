package ru.itis.api.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.request.ChatRequest;
import ru.itis.dto.response.ChatResponse;

import java.util.UUID;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RequestMapping("/api/v1/chats")
public interface ChatApi {

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<UUID> createChat(@RequestBody ChatRequest chatRequest);

    @GetMapping(value = "/{chat-id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<ChatResponse> getChat(@PathVariable("chat-id") UUID chatId);

    @PutMapping(value = "/{chat-id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<ChatResponse> updateChat(@PathVariable("chat-id") UUID chatId,
                                            @RequestBody ChatRequest chatRequest);

    @DeleteMapping(value = "/{chat-id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<?> deleteChat(@PathVariable("chat-id") UUID chatId);

    @PostMapping(value = "/moderators", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ChatResponse> addModerator(UUID chatId, UUID userId);

    @DeleteMapping(value = "/moderators", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<ChatResponse> deleteModerator(UUID chatId, UUID userId);

    @PostMapping(value = "/mutes", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ChatResponse> muteUser(UUID chatId, UUID userId);

    @DeleteMapping(value = "/mutes", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<ChatResponse> unmuteUser(UUID chatId, UUID userId);
}
