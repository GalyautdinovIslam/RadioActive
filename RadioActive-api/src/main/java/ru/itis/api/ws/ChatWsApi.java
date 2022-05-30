package ru.itis.api.ws;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;

import java.util.UUID;

import static ru.itis.constant.RadioActiveConstants.GET_MESSAGE_TOPIC;
import static ru.itis.constant.RadioActiveConstants.SEND_MESSAGE_TOPIC;

public interface ChatWsApi {
    @MessageMapping(SEND_MESSAGE_TOPIC)
    void sendMessage(@DestinationVariable("chat-id") UUID chatId, String content);

    @SubscribeMapping(GET_MESSAGE_TOPIC)
    Object getMessage(@DestinationVariable("chat-id") UUID chatId,
                      @Header String sessionId);
}
