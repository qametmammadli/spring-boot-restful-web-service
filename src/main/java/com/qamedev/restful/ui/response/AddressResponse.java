package com.qamedev.restful.ui.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {
    private String addressId;
    private String country;
    private String city;
    private String addressName;
    private String postalCode;
    private String addressType;
}
