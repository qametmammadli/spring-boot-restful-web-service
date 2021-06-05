package com.qamedev.restful.mapper;

import com.qamedev.restful.dto.UserDto;
import com.qamedev.restful.entity.UserEntity;
import com.qamedev.restful.ui.request.UserDetailsRequest;
import com.qamedev.restful.ui.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto mapUserRequestToUserDto(UserDetailsRequest request);

    UserEntity mapUserDtoToUserEntity(UserDto userDto);

    UserDto mapUserEntityToUserDto(UserEntity userEntity);

    UserResponse mapUserDtoToUserResponse(UserDto userDto);
}

