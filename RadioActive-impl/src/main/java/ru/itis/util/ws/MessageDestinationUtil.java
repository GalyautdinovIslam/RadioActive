package ru.itis.util.ws;

import lombok.experimental.UtilityClass;
import ru.itis.constant.RadioActiveConstants;

import java.util.UUID;

@UtilityClass
public class MessageDestinationUtil {
    public static String getChatDestination(UUID chatId) {
        return RadioActiveConstants.GET_MESSAGE_TOPIC.replace("{chatEntity-id}", chatId.toString());
    }

    public static String getRoomDestination(UUID roomId) {
        return RadioActiveConstants.GET_AUDIO_TOPIC.replace("{roomEntity-id}", roomId.toString());
    }
}
