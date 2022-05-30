package ru.itis.services;

import org.springframework.security.core.Authentication;
import ru.itis.dto.request.MessageRequest;
import ru.itis.dto.response.MessageResponse;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    UUID createMessage(MessageRequest messageRequest);

    MessageResponse getMessage(UUID messageId);

    MessageResponse updateMessage(UUID messageId, MessageRequest messageRequest);

    void deleteMessage(UUID messageId);

    void sendMessage(UUID chatId, String content);

    List<MessageResponse> getLastMessages(UUID chatId, int amount);
}
