package ru.itis.constants;

public interface RadioActiveConstants {
    String SEND_MESSAGE_TOPIC = "/topic/chats.{chat-id}.messages.send";
    String GET_MESSAGE_TOPIC = "/topic/chats.{chat-id}.messages.get";
    int defaultSearchPageSize = 16;
    int lastChatMessagesAmount = 250;
}
