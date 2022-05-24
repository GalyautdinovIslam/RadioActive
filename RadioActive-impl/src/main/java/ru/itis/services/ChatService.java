package ru.itis.services;

import ru.itis.dto.request.ChatRequest;
import ru.itis.dto.response.ChatResponse;

import java.util.UUID;

public interface ChatService {
    UUID createChat(ChatRequest chatRequest);

    ChatResponse getChat(UUID chatId);

    ChatResponse updateChat(UUID chatId, ChatRequest chatRequest);

    void deleteChat(UUID chatId);

    ChatResponse addModerator(UUID chatId, UUID userId);

    ChatResponse deleteModerator(UUID chatId, UUID userId);

    ChatResponse muteUser(UUID chatId, UUID userId);

    ChatResponse unmuteUser(UUID chatId, UUID userId);
}
