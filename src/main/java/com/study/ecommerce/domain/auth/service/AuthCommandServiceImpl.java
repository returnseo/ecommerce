package com.study.ecommerce.domain.auth.service;

import com.study.ecommerce.domain.auth.dto.resp.TokenResponse;
import com.study.ecommerce.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService{
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public TokenResponse generateToken(String email, String role) {
        return null;
    }

    @Override
    public void invalidateToken(String token) {

    }

    @Override
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return false;
    }

    @Override
    public String encodePassword(String rawPassword) {
        return "";
    }
}
