package com.kodera.authservice.controller;

import com.kodera.authservice.config.JwtProperties;
import com.kodera.authservice.dto.AuthResponse;
import com.kodera.authservice.dto.LoginRequest;
import com.kodera.authservice.dto.RefreshTokenRequest;
import com.kodera.authservice.dto.RegisterRequest;
import com.kodera.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProperties jwtProperties;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request)
    {
        authService.register(request);
        return ResponseEntity.ok("User Registered Successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
    {
        AuthResponse response =  authService.login(request);
        return  ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

}
