package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.request.MessageRequest;
import ru.itis.dto.response.MessageResponse;

import java.util.UUID;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RequestMapping("api/v1/messages")
public interface MessageApi {

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<UUID> createMessage(@RequestBody MessageRequest messageRequest);

    @GetMapping(value = "/{message-id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<MessageResponse> getMessage(@PathVariable("message-id") UUID messageId);

    @PutMapping(value = "/{message-id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<MessageResponse> updateMessage(@PathVariable("message-id") UUID messageId,
                                                  @RequestBody MessageRequest messageRequest);

    @DeleteMapping(value = "/{message-id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<?> deleteMessage(@PathVariable("message-id") UUID messageId);
}
