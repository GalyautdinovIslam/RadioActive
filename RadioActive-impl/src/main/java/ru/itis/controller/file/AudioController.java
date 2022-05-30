package ru.itis.controller.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.service.AudioInfoService;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
@RequestMapping("/audios")
public class AudioController {

    private final AudioInfoService audioInfoService;

    @GetMapping("/{audio-name:.+}")
    public void getAudio(@PathVariable("audio-name") String audioName, HttpServletResponse response) {
        audioInfoService.addAudioToResponse(audioName, response);
    }

}
