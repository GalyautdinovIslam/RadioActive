package ru.itis.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.exception.AudioNotFoundException;
import ru.itis.model.AudioInfoEntity;
import ru.itis.repository.AudioInfoRepository;
import ru.itis.service.AudioInfoService;

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
        AudioInfoEntity audioInfoEntity = audioInfoRepository.findByStorageName(audioName)
                .orElseThrow(AudioNotFoundException::new);

        response.setContentType(audioInfoEntity.getType());
        response.setContentLength(audioInfoEntity.getSize().intValue());
        response.setHeader("Content-Disposition", "filename=\"" + audioInfoEntity.getOriginalName() + "\"");

        try {
            IOUtils.copy(new FileInputStream(storagePath + "\\" + audioInfoEntity.getStorageName()),
                    response.getOutputStream());

        } catch (IOException e) {
            throw new AudioNotFoundException(e);
        }

    }
}
