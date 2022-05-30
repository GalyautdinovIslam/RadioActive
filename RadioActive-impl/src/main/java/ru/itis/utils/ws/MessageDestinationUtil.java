package ru.itis.utils.ws;

import lombok.experimental.UtilityClass;
import ru.itis.constants.RadioActiveConstants;

import java.util.UUID;

@UtilityClass
public class MessageDestinationUtil {
    public static String getChatDestination(UUID chatId) {
        return RadioActiveConstants.GET_MESSAGE_TOPIC.replace("{chat-id}", chatId.toString());
    }
    public static String getRoomDestination(UUID roomId) {
        return RadioActiveConstants.GET_AUDIO_TOPIC.replace("{room-id}", roomId.toString());
    }
}
