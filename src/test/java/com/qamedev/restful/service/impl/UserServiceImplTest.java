package com.qamedev.restful.service.impl;

import com.qamedev.restful.dto.UserDto;
import com.qamedev.restful.entity.TokenEntity;
import com.qamedev.restful.entity.UserEntity;
import com.qamedev.restful.exception.UserServiceException;
import com.qamedev.restful.repository.UserRepository;
import com.qamedev.restful.service.MailService;
import com.qamedev.restful.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceImplTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    TokenService tokenService;

    @MockBean
    MailService mailService;

    @MockBean
    ModelMapper mapper;

    @InjectMocks
    UserServiceImpl userService;

    private final List<UserEntity> userEntityList = new ArrayList<>();
    private final List<UserDto> userDtoList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Qame");
        userEntity.setSurName("Mammadli");
        userEntity.setEmail("test@gmail.com");
        userEntity.setPassword("12345");
        userEntity.setUserId("wDrFte23eWs");
        userEntity.setStatus(0);
        userEntity.setAddresses(new ArrayList<>());
        userEntityList.add(userEntity);

        userService = new UserServiceImpl(
                userRepository,
                bCryptPasswordEncoder,
                tokenService,
                mailService,
                mapper);

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("Qamet");
        userDto.setSurName("Mammadli");
        userDto.setEmail("test@gmail.com");
        userDto.setPassword("12345");
        userDto.setAddresses(new ArrayList<>());
        userDtoList.add(userDto);

    }

    @Test
    void testGetUserByEmail() {
        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(userEntityList.get(0)));

        when(mapper.map(userEntityList.get(0), UserDto.class)).thenReturn(userDtoList.get(0));

        UserDto userDto = userService.getUserByEmail("test@gmail.com");

        assertNotNull(userDto);
        assertEquals("Qamet", userDto.getFirstName());
        assertEquals("Mammadli", userDto.getSurName());
        assertEquals("test@gmail.com", userDto.getEmail());

        verify(userRepository).findByEmail("test@gmail.com");
    }

    @Test
    void testGetUserByEmailNotFound(){
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByEmail(anyString()));

        verify(userRepository).findByEmail(anyString());
    }


    @Test
    void testCreateUser(){
        UserDto userDto = userDtoList.get(0);
        UserEntity userEntity = userEntityList.get(0);
        TokenEntity tokenEntity = new TokenEntity();

        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(mapper.map(userDto, UserEntity.class)).thenReturn(userEntityList.get(0));
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(userDto.getPassword());
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(tokenService.createActivationToken(any(UserEntity.class))).thenReturn(tokenEntity);
//        Mockito.doNothing().when(mailService).sendConfirmationEmail(tokenEntity);
        when(mapper.map(userEntity, UserDto.class)).thenReturn(userDtoList.get(0));

        UserDto createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals("Qamet", createdUser.getFirstName());
        assertEquals("test@gmail.com", createdUser.getEmail());
        assertEquals(userDto.getAddresses().size(), createdUser.getAddresses().size());

        verify(bCryptPasswordEncoder, times(1)).encode(userDto.getPassword());
        verify(userRepository).save(any(UserEntity.class));
        verify(mailService, times(1)).sendConfirmationEmail(tokenEntity);
    }

    @Test
    void testCreateUserAlreadyExists(){
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(UserServiceException.class, () -> userService.createUser(userDtoList.get(0)));

        verify(userRepository).existsByEmail(anyString());
    }
}