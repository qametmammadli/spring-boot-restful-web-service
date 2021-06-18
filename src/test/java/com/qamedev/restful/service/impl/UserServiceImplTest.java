package com.qamedev.restful.service.impl;

import com.qamedev.restful.dto.UserDto;
import com.qamedev.restful.entity.UserEntity;
import com.qamedev.restful.repository.UserRepository;
import com.qamedev.restful.service.MailService;
import com.qamedev.restful.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
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
import static org.mockito.Mockito.when;


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
        userDtoList.add(userDto);

        when(mapper.map(any(), any())).thenReturn(userDtoList.get(0));
    }

    @Test
    void testGetUserByEmail() {
        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(userEntityList.get(0)));

        UserDto userDto = userService.getUserByEmail("test@gmail.com");

        assertNotNull(userDto);
        assertEquals("Qamet", userDto.getFirstName());
        assertEquals("Mammadli", userDto.getSurName());
        assertEquals("test@gmail.com", userDto.getEmail());

        Mockito.verify(userRepository).findByEmail("test@gmail.com");
    }

    @Test
    void testGetUserByEmailNotFound(){
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByEmail(anyString()));

        Mockito.verify(userRepository).findByEmail(anyString());
    }
}