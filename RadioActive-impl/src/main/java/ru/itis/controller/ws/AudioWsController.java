package ru.itis.controller.ws;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.itis.api.ws.AudioWsApi;

import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class AudioWsController implements AudioWsApi {

    @Override
    public String getAudio(UUID roomId, String sessionId) {
        return null;
    }
}
