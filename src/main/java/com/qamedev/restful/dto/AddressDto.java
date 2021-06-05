package com.qamedev.restful.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddressDto implements Serializable {
    private static final long serialVersionUID = -537681513028180998L;
    private String addressId;
    private String country;
    private String city;
    private String addressName;
    private String postalCode;
    private String addressType;
    private UserDto user;

    @Override
    public String toString() {
        return "AddressDto{" +
                "addressId='" + addressId + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", addressName='" + addressName + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", addressType='" + addressType + '\'' +
                '}';
    }
}
