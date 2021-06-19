package com.qamedev.restful.service.impl;

import com.qamedev.restful.dto.AddressDto;
import com.qamedev.restful.dto.UserDto;
import com.qamedev.restful.dto.UserStatus;
import com.qamedev.restful.entity.TokenEntity;
import com.qamedev.restful.entity.UserEntity;
import com.qamedev.restful.exception.ErrorMessages;
import com.qamedev.restful.exception.UserServiceException;
import com.qamedev.restful.repository.UserRepository;
import com.qamedev.restful.security.UserPrincipal;
import com.qamedev.restful.service.MailService;
import com.qamedev.restful.service.TokenService;
import com.qamedev.restful.service.UserService;
import com.qamedev.restful.util.CommonUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;
    private final MailService mailService;
    private final ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenService tokenService, MailService mailService, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenService = tokenService;
        this.mailService = mailService;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserServiceException(ErrorMessages.REGISTERED_EMAIL.getErrorMessage());
        }

        for (AddressDto addressDto : userDto.getAddresses()) {
            addressDto.setAddressId(CommonUtil.generateAddressId());
            addressDto.setUser(userDto);
        }

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setUserId(CommonUtil.generateUserId());
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        userEntity.setStatus(UserStatus.PENDING.getStatusId());
        userEntity = userRepository.save(userEntity);

        TokenEntity tokenEntity = tokenService.createActivationToken(userEntity);

        mailService.sendConfirmationEmail(tokenEntity);

        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
        if (optionalUserEntity.isEmpty())
            throw new UsernameNotFoundException("User with email " + email + " not found");

        return new UserPrincipal(optionalUserEntity.get());
    }

    @Override
    @Transactional
    public UserDto getUserByEmail(String email) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
        if (optionalUserEntity.isEmpty())
            throw new UsernameNotFoundException("User with email " + email + " not found");

        return mapper.map(optionalUserEntity.get(), UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserId(id);
        if (optionalUserEntity.isEmpty())
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

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
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        userRepository.delete(optionalUserEntity.get());
    }

    @Override
    public Page<UserDto> getUsers(PageRequest pageRequest) {
        Page<UserEntity> userEntityPage = userRepository.findAll(pageRequest);
        return userEntityPage.map(userEntity -> mapper.map(userEntity, UserDto.class));
    }

    @Override
    @Transactional
    public boolean activateUser(String token) {
        TokenEntity tokenEntity = tokenService.checkToken(token);

        UserEntity userEntity = tokenEntity.getUser();
        userEntity.setStatus(UserStatus.ACTIVE.getStatusId());
        userRepository.save(userEntity);
        tokenService.deleteToken(tokenEntity);
        return true;
    }

    @Override
    public boolean passwordReset(String email) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
        if(optionalUserEntity.isEmpty()){
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        TokenEntity tokenEntity = tokenService.createPasswordResetToken(optionalUserEntity.get());
        mailService.sendPasswordResetEmail(tokenEntity);
        return true;
    }


    @Override
    @Transactional
    public boolean savePassword(String token, String password) {
        TokenEntity tokenEntity = tokenService.checkToken(token);

        UserEntity userEntity = tokenEntity.getUser();

        userEntity.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(userEntity);
        tokenService.deleteToken(tokenEntity);
        return true;
    }
}
