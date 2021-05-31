package com.qamedev.restful.ui.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponsePayload {
    private final LocalDateTime date;
    private final String message;
    private final String path;
}
