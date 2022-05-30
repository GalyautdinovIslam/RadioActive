package ru.itis.service;

import javax.servlet.http.HttpServletResponse;

public interface AudioInfoService {
    void addAudioToResponse(String audioName, HttpServletResponse response);
}
