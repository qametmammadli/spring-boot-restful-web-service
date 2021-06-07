package com.qamedev.restful.service;

import com.qamedev.restful.dto.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> getAddressesByUserId(String userId);

    AddressDto getAddress(String userId, String addressId);
}
