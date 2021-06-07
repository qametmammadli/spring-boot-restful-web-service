package com.qamedev.restful.service.impl;

import com.qamedev.restful.dto.AddressDto;
import com.qamedev.restful.dto.UserDto;
import com.qamedev.restful.entity.UserEntity;
import com.qamedev.restful.exception.ErrorMessages;
import com.qamedev.restful.exception.UserServiceException;
import com.qamedev.restful.mapper.UserMapper;
import com.qamedev.restful.repository.UserRepository;
import com.qamedev.restful.service.UserService;
import com.qamedev.restful.util.CommonUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserServiceException(ErrorMessages.REGISTERED_EMAIL.getErrorMessage());
        }

        userDto.setUserId(CommonUtil.generateUserId());
        userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        for (AddressDto addressDto : userDto.getAddresses()) {
            addressDto.setAddressId(CommonUtil.generateAddressId());
            addressDto.setUser(userDto);
        }

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity = userRepository.save(userEntity);

        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
        if (optionalUserEntity.isEmpty())
            throw new UsernameNotFoundException("User with email " + email + " not found");

        UserEntity userEntity = optionalUserEntity.get();
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new HashSet<>());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
        if (optionalUserEntity.isEmpty())
            throw new UsernameNotFoundException("User with email " + email + " not found");
        System.out.println(" entity  : " + optionalUserEntity.get().getEmail());
        return UserMapper.INSTANCE.mapEntityToDtoWithoutAddresses(optionalUserEntity.get());
//        return mapper.map(optionalUserEntity.get(), UserDto.class);
//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(optionalUserEntity.get(), userDto);
//        System.out.println(" dtos :" + optionalUserEntity);
//        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserId(id);
        if (optionalUserEntity.isEmpty())
//            throw new UsernameNotFoundException("User not found");
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
//        return UserMapper.INSTANCE.mapUserEntityToUserDto(optionalUserEntity.get());
        return mapper.map(optionalUserEntity.get(), UserDto.class);
    }

    @Override
    @Transactional
    public UserDto updateUser(String userId, UserDto userDto) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserId(userId);
        if (optionalUserEntity.isEmpty())
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        UserEntity userEntity = optionalUserEntity.get();
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setSurName(userDto.getSurName());

        userEntity = userRepository.save(userEntity);

        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserId(id);
        if(optionalUserEntity.isEmpty()){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with id %s not found", id));
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        userRepository.delete(optionalUserEntity.get());
    }

    @Override
    public Page<UserDto> getUsers(PageRequest pageRequest) {
        Page<UserEntity> userEntityPage = userRepository.findAll(pageRequest);
//        return userEntityPage.map(UserMapper.INSTANCE::mapUserEntityToUserDto);
        return userEntityPage.map(userEntity -> mapper.map(userEntity, UserDto.class));
    }
}
