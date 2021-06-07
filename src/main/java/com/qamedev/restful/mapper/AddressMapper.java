package com.qamedev.restful.mapper;

import com.qamedev.restful.dto.AddressDto;
import com.qamedev.restful.entity.AddressEntity;
import com.qamedev.restful.ui.request.AddressRequest;
import com.qamedev.restful.ui.response.AddressResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    @Mapping(target = "user", ignore = true)
    AddressDto mapAddressEntityToAddressDto(AddressEntity entity);

    AddressDto mapAddressRequestToAddressDto(AddressRequest request);

    AddressResponse mapAddressDtoToAddressResponse(AddressDto dto);

    AddressEntity mapAddressDtoToAddressEntity(AddressDto dto);
}
