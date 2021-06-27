package com.qamedev.restful.util;

import com.qamedev.restful.security.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, Constants.TOKEN_SECRET)
                .compact();
    }

    public static String getUsernameByToken(String token) {
        return getClaims(token).getSubject();
    }

    public static boolean isTokenExpired(String token) {
        try{
            Claims claims = getClaims(token);

            Date tokenExpirationDate = claims.getExpiration();
            return tokenExpirationDate.before(new Date());
        } catch(ExpiredJwtException e){
            return true;
        }
    }

    private static Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(Constants.TOKEN_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public static String generateActivationToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EMAIL_VERIFY_TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, Constants.TOKEN_SECRET)
                .compact();
    }

    public static String generatePasswordResetToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + Constants.PASSWORD_RESET_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, Constants.TOKEN_SECRET)
                .compact();
    }
}
