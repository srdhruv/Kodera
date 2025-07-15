package com.kodera.userservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getSigningKey()
    {
        System.out.println("JWT Key  is " + jwtSecret);
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims extractAllClaims(String token)
    {
        return Jwts.parser().verifyWith(getSigningKey())
                .build().parseSignedClaims(token).getPayload();
    }

    public long extractUserId(String token)
    {
        Claims claims = extractAllClaims(token);
        String userIdStr = claims.get("userId").toString();
        return Long.parseLong(userIdStr);
    }

    public boolean isTokenValid(String token)
    {
        try
        {
            extractAllClaims(token);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

}
