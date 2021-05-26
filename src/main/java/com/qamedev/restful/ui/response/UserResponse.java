package com.qamedev.restful.ui.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String userId;
    private String firstName;
    private String surName;
    private String email;
}
