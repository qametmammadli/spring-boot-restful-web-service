package com.qamedev.restful.mapper;

import com.qamedev.restful.dto.AddressDto;
import com.qamedev.restful.dto.UserDto;
import com.qamedev.restful.entity.AddressEntity;
import com.qamedev.restful.entity.UserEntity;
import com.qamedev.restful.ui.request.UserDetailsRequest;
import com.qamedev.restful.ui.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "user.addresses", ignore = true)
    AddressDto mapAddressEntityToDto(AddressEntity entity);

    @Mapping(target = "addresses", ignore = true)
    UserDto mapEntityToDtoWithoutAddresses(UserEntity userEntity);

    UserDto mapUserRequestToUserDto(UserDetailsRequest request);

    UserEntity mapUserDtoToUserEntity(UserDto userDto);

    UserDto mapUserEntityToUserDto(UserEntity userEntity);

    UserResponse mapUserDtoToUserResponse(UserDto userDto);
}

