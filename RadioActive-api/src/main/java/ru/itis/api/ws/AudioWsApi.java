package ru.itis.api.ws;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.annotation.SubscribeMapping;

import java.util.UUID;

import static ru.itis.constant.RadioActiveConstants.GET_AUDIO_TOPIC;

public interface AudioWsApi {
    @SubscribeMapping(GET_AUDIO_TOPIC)
    String getAudio(@DestinationVariable("room-id") UUID roomId, @Header String sessionId);
}
