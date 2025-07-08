package com.study.ecommerce.domain.auth.service;

import com.study.ecommerce.domain.auth.dto.resp.TokenResponse;

public interface AuthCommandService {

    TokenResponse generateToken(String email, String role);

    void invalidateToken(String token);

    boolean validatePassword(String rawPassword, String encodedPassword);

    String encodePassword(String rawPassword);
}
