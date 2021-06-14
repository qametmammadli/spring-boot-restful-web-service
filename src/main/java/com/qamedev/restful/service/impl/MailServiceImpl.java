package com.qamedev.restful.service.impl;

import com.qamedev.restful.entity.TokenEntity;
import com.qamedev.restful.entity.UserEntity;
import com.qamedev.restful.service.MailService;
import com.qamedev.restful.util.MailUtil;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    public static final int numberOfThreads = 5;

    //     This statement create a thread pool of 5 fixed threads
    //     i am sending mail using ScheduledExecutorService.submit();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(numberOfThreads);

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
//    @Async  // another way with spring
    public void sendConfirmationEmail(TokenEntity tokenEntity) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mail = new MimeMessageHelper(mimeMessage, "utf-8");
            UserEntity user = tokenEntity.getUser();
            mail.setFrom("rest-api@gmail.com"); // todo get from config
            mail.setTo(user.getEmail());
            mail.setSubject(MailUtil.EMAIL_CONFIRMATION_SUBJECT);
            mail.setText(MailUtil.getEmailConfirmationBody(user.getFirstName(), user.getSurName(), tokenEntity.getToken()), true);
            executorService.submit(() -> mailSender.send(mimeMessage));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
