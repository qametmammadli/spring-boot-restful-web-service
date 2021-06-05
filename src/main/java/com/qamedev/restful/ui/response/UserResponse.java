package com.qamedev.restful.ui.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {
    private String userId;
    private String firstName;
    private String surName;
    private String email;
    private List<AddressResponse> addresses;
}
