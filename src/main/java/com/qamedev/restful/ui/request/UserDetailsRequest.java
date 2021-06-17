package com.qamedev.restful.ui.request;

import lombok.Data;

import java.util.List;

@Data
public class UserDetailsRequest {
    private String firstName;
    private String surName;
    private String email;
    private String password;
    private List<AddressRequest> addresses;
}
