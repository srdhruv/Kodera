package com.kodera.authservice.service;

import com.kodera.authservice.dto.AuthResponse;
import com.kodera.authservice.dto.LoginRequest;
import com.kodera.authservice.dto.RegisterRequest;
import com.kodera.authservice.entity.Role;
import com.kodera.authservice.entity.User;
import com.kodera.authservice.mapper.UserMapper;
import com.kodera.authservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    private JwtService jwtService;

    public void register(RegisterRequest request)
    {
        if(userRepository.existsByEmail(request.getEmail()))
        {
            throw new RuntimeException("Email already exists. Please use different email.");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }
    public AuthResponse login(LoginRequest request)
    {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Credentials"));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new RuntimeException("Invalid Credentials");
        }

        String accessToken = jwtService.generateAccessToken(request.getEmail());
        String refreshToken = jwtService.generateRefreshToken(request.getEmail());
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(String refreshToken) {
        String email = jwtService.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateAccessToken(user.getEmail());
        String newRefreshToken = jwtService.generateRefreshToken(user.getEmail());
        ;
        return new AuthResponse(newAccessToken, newRefreshToken);
    }
}
