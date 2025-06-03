package com.study.ecommerce.domain.auth.dto.resp;

public record TokenResponse(
        String accessToken,
        String tokenType,
        String email,
        String name,
        String role
) {
}
