package com.qamedev.restful.ui.request;

import lombok.Getter;

@Getter
public class PasswordResetRequest {
    private String email;
    private String token;
    private String password;
}
