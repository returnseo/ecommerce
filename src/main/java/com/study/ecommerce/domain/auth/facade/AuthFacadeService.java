package com.study.ecommerce.domain.auth.facade;

import com.study.ecommerce.domain.auth.dto.req.LoginRequest;
import com.study.ecommerce.domain.auth.dto.req.SignUpRequest;
import com.study.ecommerce.domain.auth.dto.resp.TokenResponse;

public interface AuthFacadeService {
    void signUp(SignUpRequest request);

    TokenResponse login(LoginRequest request);
}
