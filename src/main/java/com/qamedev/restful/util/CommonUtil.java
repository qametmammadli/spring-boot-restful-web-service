package com.qamedev.restful.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class CommonUtil {

    private static final Random RANDOM = new SecureRandom();

    public static String generateUserId(int length) {
        return generateRandomString(length);
    }

    private static String generateRandomString(int length) {
        StringBuilder builder = new StringBuilder(length);
        String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        for (int i = 0; i < length; i++) {
            builder.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return String.valueOf(builder);
    }
}
