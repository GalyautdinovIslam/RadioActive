package ru.itis.utils.ws;

import lombok.experimental.UtilityClass;
import ru.itis.constants.RadioActiveConstants;

import java.util.UUID;

@UtilityClass
public class MessageDestinationUtils {
    public static String getDestination(UUID chatId) {
        return RadioActiveConstants.GET_MESSAGE_TOPIC.replace("{chat-id}", chatId.toString());
    }
}
