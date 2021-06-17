package com.qamedev.restful.security;

public class Constants {
    public static final long EXPIRATION_TIME = 10 * 86400 * 1000; // 10 days
    public static final long EMAIL_VERIFY_TOKEN_EXPIRATION_TIME = 3 * 86400 * 1000; // 3 days
    public static final long PASSWORD_RESET_EXPIRATION_TIME = 3 * 3600 * 1000; // 3 hours
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_KEY = "Authorization";
    public static final String TOKEN_SECRET = "q3eEftWqaS34oiU6HqUi7";
}
