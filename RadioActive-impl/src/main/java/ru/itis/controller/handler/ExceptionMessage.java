package ru.itis.controller.handler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionMessage {
    private String endpoint;
    private String message;
    private String detailMessage;
    private String exceptionName;
}
