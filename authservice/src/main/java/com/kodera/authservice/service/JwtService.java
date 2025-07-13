package com.kodera.authservice.service;

import com.kodera.authservice.config.JwtProperties;
import com.kodera.authservice.entity.User;
import com.kodera.authservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    private SecretKey secretKey;

    @PostConstruct
    private void initKey()
    {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
    public String generateAccessToken(String email) {
        return generateToken(email, jwtProperties.getAccessTokenExpirationMs());
    }

    public String generateRefreshToken(String email) {
        return generateToken(email, jwtProperties.getRefreshTokenExpirationMs());
    }
    public String generateToken(String email, long expirationTimeMs)
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        return buildToken(claims, user.getEmail(), expirationTimeMs);
    }
    private String buildToken(Map<String, Object> extraClaims, String subject, long expirationTimeMS)
    {
        return Jwts.builder().claims(extraClaims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTimeMS))
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }
    public String extractEmail(String token)
    {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    private SecretKey getSecretKey()
    {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String extractUsername(String token)
    {
        return Jwts.parser().verifyWith(getSecretKey()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }
    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token)
    {
        return Jwts.parser().verifyWith(getSecretKey())
                .build().parseSignedClaims(token).getPayload().getExpiration();
    }

    public Claims extactAllClaims(String jwt) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(jwt).getPayload();
    }
}
