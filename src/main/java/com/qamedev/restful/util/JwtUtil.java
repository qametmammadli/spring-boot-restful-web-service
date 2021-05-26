package com.qamedev.restful.util;

import com.qamedev.restful.security.Constants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    public static String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512,  Constants.TOKEN_SECRET)
                .compact();
    }

    public static String getUsernameByToken(String token){
        return Jwts.parser()
                .setSigningKey(Constants.TOKEN_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
