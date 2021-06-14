package com.qamedev.restful.service.impl;

import com.qamedev.restful.dto.TokenType;
import com.qamedev.restful.entity.TokenEntity;
import com.qamedev.restful.entity.UserEntity;
import com.qamedev.restful.repository.TokenRepository;
import com.qamedev.restful.service.TokenService;
import com.qamedev.restful.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public TokenEntity createActivationToken(UserEntity user) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setName(TokenType.ACTIVATION);
        tokenEntity.setToken(JwtUtil.generateActivationToken(user.getUserId()));
        tokenEntity.setUser(user);
        return tokenRepository.save(tokenEntity);
    }

    @Override
    public Optional<TokenEntity> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void deleteToken(TokenEntity token) {
        tokenRepository.delete(token);
    }
}
