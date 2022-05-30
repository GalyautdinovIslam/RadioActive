package ru.itis.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.exceptions.AudioNotFoundException;
import ru.itis.models.AudioInfo;
import ru.itis.repositories.AudioInfoRepository;
import ru.itis.services.AudioInfoService;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class AudioInfoServiceImpl implements AudioInfoService {

    @Value("${audios.storage.path}")
    private String storagePath;


    private final AudioInfoRepository audioInfoRepository;

    @Override
    public void addAudioToResponse(String audioName, HttpServletResponse response) {
        AudioInfo audioInfo = audioInfoRepository.findByStorageName(audioName)
                .orElseThrow(AudioNotFoundException::new);

        response.setContentType(audioInfo.getType());
        response.setContentLength(audioInfo.getSize().intValue());
        response.setHeader("Content-Disposition", "filename=\"" + audioInfo.getOriginalName() + "\"");

        try {
            IOUtils.copy(new FileInputStream(storagePath + "\\" + audioInfo.getStorageName()),
                    response.getOutputStream());

        } catch (IOException e) {
            throw new AudioNotFoundException(e);
        }

    }
}
