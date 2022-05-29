package ru.itis.controllers.ws;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Controller;
import ru.itis.api.ws.ChatWsApi;
import ru.itis.constants.RadioActiveConstants;
import ru.itis.services.ChatService;
import ru.itis.services.MessageService;

import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class ChatWsController implements ChatWsApi {
    private final MessageService messageService;
    private final ChatService chatService;

    @Override
    public void sendMessage(UUID chatId, String content) {
        messageService.sendMessage(chatId, content);
    }

    @Override
    public Object getMessage(UUID chatId,
                             String sessionId) {
        chatService.connectToChat(chatId, sessionId);
        return messageService.getLastMessages(chatId, RadioActiveConstants.lastChatMessagesAmount);
    }
}
