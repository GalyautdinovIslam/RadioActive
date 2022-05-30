package ru.itis.controllers.ws;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.itis.api.ws.AudioWsApi;
import ru.itis.api.ws.ChatWsApi;
import ru.itis.constants.RadioActiveConstants;
import ru.itis.services.ChatService;
import ru.itis.services.MessageService;

import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class AudioWsController implements AudioWsApi {

    @Override
    public String getAudio(UUID roomId, String sessionId) {
        return null;
    }
}
