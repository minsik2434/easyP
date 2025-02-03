package com.easy_p.easyp.common.jwt;

import com.easy_p.easyp.common.exception.JwtTokenException;
import io.jsonwebtoken.*;
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

    public String getSub(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key).
                build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getClaim(String token, String claimKey){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(claimKey, String.class);
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        }catch(SecurityException | MalformedJwtException e){
            throw new JwtTokenException("Invalid Token");
        } catch (ExpiredJwtException e){
            throw new JwtTokenException("Expired Token");
        } catch (UnsupportedJwtException e){
            throw new JwtTokenException("Unsupported Token");
        } catch (IllegalArgumentException e){
            throw new JwtTokenException("Empty Token body");
        }
        return true;
    }

    private String genAccessToken(String email, long now){
        return Jwts.builder()
                .setSubject("access-token")
                .claim("email", email)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String genRefreshToken(String email, long now){
        return Jwts.builder()
                .setSubject("refresh-token")
                .claim("email", email)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
