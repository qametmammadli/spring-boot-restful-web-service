package com.qamedev.restful.service.impl;

import com.qamedev.restful.dto.UserDto;
import com.qamedev.restful.entity.UserEntity;
import com.qamedev.restful.repository.UserRepository;
import com.qamedev.restful.service.UserService;
import com.qamedev.restful.util.CommonUtil;
import org.springframework.beans.BeanUtils;
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

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        if(userRepository.findByEmail(userDto.getEmail()).isPresent())
            throw new RuntimeException("This user already exists");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);
        userEntity.setUserId(CommonUtil.generateUserId(35));
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        userRepository.save(userEntity);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);

        if(optionalUserEntity.isEmpty())
            throw new UsernameNotFoundException("User with email " + email + " not found");

        UserEntity userEntity = optionalUserEntity.get();
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new HashSet<>());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
        if(optionalUserEntity.isEmpty())
            throw new UsernameNotFoundException("User with email " + email + " not found");

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(optionalUserEntity.get(), userDto);
        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String id) {
        UserDto userDto = new UserDto();
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserId(id);
        if(optionalUserEntity.isEmpty())
            throw new UsernameNotFoundException("User not found");

        BeanUtils.copyProperties(optionalUserEntity.get(), userDto);
        return userDto;
    }
}
