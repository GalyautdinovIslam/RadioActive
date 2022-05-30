package ru.itis.controller.ws;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.itis.api.ws.ChatWsApi;
import ru.itis.constant.RadioActiveConstants;
import ru.itis.service.ChatService;
import ru.itis.service.MessageService;

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
    public Object getMessage(UUID chatId, String sessionId) {
        chatService.connectToChat(chatId, sessionId);
        return messageService.getLastMessages(chatId, RadioActiveConstants.lastChatMessagesAmount);
    }
}
