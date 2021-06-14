package com.qamedev.restful.service;

import com.qamedev.restful.entity.TokenEntity;

public interface MailService {
    void sendConfirmationEmail(TokenEntity tokenEntity);
}
