package com.qamedev.restful.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String userId;
    private String firstName;
    private String surName;
    private String email;
    private String password;
    private int status = 0;
    private List<AddressDto> addresses;
}
