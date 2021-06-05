package com.qamedev.restful.ui.request;

import lombok.Data;

@Data
public class AddressRequest {
    private String country;
    private String city;
    private String addressName;
    private String postalCode;
    private String addressType;
}
