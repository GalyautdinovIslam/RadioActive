package ru.itis.services;

import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
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

    void connectToChat(UUID chatId, String sessionId);

    @EventListener
    void handleUnsubscribe(SessionUnsubscribeEvent event);

    @EventListener
    void handleDisconnect(SessionDisconnectEvent event);
}
