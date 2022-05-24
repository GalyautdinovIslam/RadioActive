package ru.itis.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.api.ChatApi;
import ru.itis.dto.request.ChatRequest;
import ru.itis.dto.response.ChatResponse;
import ru.itis.services.ChatService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ChatController implements ChatApi {

    private final ChatService chatService;

    @Override
    public ResponseEntity<UUID> createChat(ChatRequest chatRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chatService.createChat(chatRequest));
    }

    @Override
    public ResponseEntity<ChatResponse> getChat(UUID chatId) {
        return ResponseEntity.ok(chatService.getChat(chatId));
    }

    @Override
    public ResponseEntity<ChatResponse> updateChat(UUID chatId, ChatRequest chatRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(chatService.updateChat(chatId, chatRequest));
    }

    @Override
    public ResponseEntity<?> deleteChat(UUID chatId) {
        chatService.deleteChat(chatId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public ResponseEntity<ChatResponse> addModerator(UUID chatId, UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chatService.addModerator(chatId, userId));
    }

    @Override
    public ResponseEntity<ChatResponse> deleteModerator(UUID chatId, UUID userId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(chatService.deleteModerator(chatId, userId));
    }

    @Override
    public ResponseEntity<ChatResponse> muteUser(UUID chatId, UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chatService.muteUser(chatId, userId));
    }

    @Override
    public ResponseEntity<ChatResponse> unmuteUser(UUID chatId, UUID userId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(chatService.unmuteUser(chatId, userId));
    }
}
