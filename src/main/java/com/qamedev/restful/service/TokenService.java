package com.qamedev.restful.service;


import com.qamedev.restful.entity.TokenEntity;
import com.qamedev.restful.entity.UserEntity;

import java.util.Optional;

public interface TokenService {
    TokenEntity createActivationToken(UserEntity user);

    Optional<TokenEntity> getToken(String token);

    void deleteToken(TokenEntity token);
}
