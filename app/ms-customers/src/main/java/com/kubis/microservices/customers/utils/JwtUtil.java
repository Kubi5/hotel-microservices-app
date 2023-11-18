package com.kubis.microservices.customers.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final long tokenValidity = 7 * 24 * 60 * 60; // in seconds, here: one week = 7 days

    public String generateToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + tokenValidity * 1000;
        Date exp = new Date(expMillis);
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(nowMillis)).setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

}
