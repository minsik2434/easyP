package com.easy_p.easyp.common.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final Key key;

    public JwtProvider(@Value("${jwt.secret}") String keyString){
        byte[] keyBytes = Decoders.BASE64.decode(keyString);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtToken createToken(String email){
        long now = System.currentTimeMillis();
        String accessToken = genAccessToken(email, now);
        String refreshToken = genRefreshToken(email, now);
        return new JwtToken(accessToken, refreshToken);
    }

    private String genAccessToken(String email, long now){
        return Jwts.builder()
                .setSubject("AccessToken")
                .claim("email", email)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String genRefreshToken(String email, long now){
        return Jwts.builder()
                .setSubject("RefreshToken")
                .claim("email", email)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
