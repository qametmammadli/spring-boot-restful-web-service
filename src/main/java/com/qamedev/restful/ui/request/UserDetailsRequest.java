package com.qamedev.restful.ui.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserDetailsRequest {
    private String firstName;
    private String surName;
    private String email;
    private String password;
}
