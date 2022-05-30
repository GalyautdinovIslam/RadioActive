package ru.itis.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.api.rest.MessageApi;
import ru.itis.dto.request.MessageRequest;
import ru.itis.dto.response.MessageResponse;
import ru.itis.service.MessageService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class MessageController implements MessageApi {

    private final MessageService messageService;

    @Override
    public ResponseEntity<UUID> createMessage(MessageRequest messageRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.createMessage(messageRequest));
    }

    @Override
    public ResponseEntity<MessageResponse> getMessage(UUID messageId) {
        return ResponseEntity.ok(messageService.getMessage(messageId));
    }

    @Override
    public ResponseEntity<MessageResponse> updateMessage(UUID messageId, MessageRequest messageRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(messageService.updateMessage(messageId, messageRequest));
    }

    @Override
    public ResponseEntity<?> deleteMessage(UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
