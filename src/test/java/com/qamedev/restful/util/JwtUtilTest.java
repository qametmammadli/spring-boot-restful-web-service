package com.qamedev.restful.util;

import com.qamedev.restful.exception.UserServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class JwtUtilTest {

    final String username = "test@gmail.com";

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGenerateToken() {
        String token = JwtUtil.generateToken(username);

        assertNotNull(token);
    }

    @Test
    void testGetUsernameByToken() {
        String token = JwtUtil.generateToken(username);
        String usernameByToken = JwtUtil.getUsernameByToken(token);

        assertNotNull(usernameByToken);
        assertEquals(username, usernameByToken);
    }

    @Test
    void testIsTokenExpired() {
        String testToken = JwtUtil.generateToken(username);
        boolean isTokenExpired = JwtUtil.isTokenExpired(testToken);
        assertFalse(isTokenExpired);
    }

    @Test
    void testTokenExpired(){
        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxQGdtYWlsLmNvbSIsImV4cCI6MTYyNDEyMzgwNX0.l1QWDAjHgmi0u-eaVNXVf2h-VWX8_vYOyZTuroRJqHyZfnQDATZXH3DLF36qTCQWPUnKMF1XRLNKwghiXhseog";

        assertThrows(UserServiceException.class, () -> JwtUtil.isTokenExpired(expiredToken));
    }
}