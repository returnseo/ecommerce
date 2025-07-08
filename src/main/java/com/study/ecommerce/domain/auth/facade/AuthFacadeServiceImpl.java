package com.study.ecommerce.domain.auth.facade;

import com.study.ecommerce.domain.auth.dto.req.LoginRequest;
import com.study.ecommerce.domain.auth.dto.req.SignUpRequest;
import com.study.ecommerce.domain.auth.dto.resp.TokenResponse;
import com.study.ecommerce.domain.auth.service.AuthCommandService;
import com.study.ecommerce.domain.member.service.command.MemberCommandService;
import com.study.ecommerce.domain.member.service.query.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacadeServiceImpl implements AuthFacadeService{
    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;
    private final AuthCommandService authCommandService;

    @Override
    public void signUp(SignUpRequest request) {
        if (memberQueryService.existsByEmail(request.email())) {

        }
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        return null;
    }
}
