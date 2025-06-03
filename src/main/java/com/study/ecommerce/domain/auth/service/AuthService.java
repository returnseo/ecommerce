package com.study.ecommerce.domain.auth.service;

import com.study.ecommerce.domain.auth.dto.req.LoginRequest;
import com.study.ecommerce.domain.auth.dto.req.SignUpRequest;
import com.study.ecommerce.domain.auth.dto.resp.TokenResponse;
import com.study.ecommerce.domain.member.entity.Member;
import com.study.ecommerce.domain.member.repository.MemberRepository;
import com.study.ecommerce.global.error.ErrorCode;
import com.study.ecommerce.global.error.exception.BusinessException;
import com.study.ecommerce.global.error.exception.EntityNotFoundException;
import com.study.ecommerce.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signUp(SignUpRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }

        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .address(request.address())
                .role(Member.Role.CUSTOMER)
                .build();

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new BadCredentialsException("로그인에 실패하였습니다.");
        }

        String token = jwtTokenProvider.createToken(
                member.getEmail(),
                Collections.singletonList(member.getRole().name())
        );

        return new TokenResponse(
                token,
                "Bearer",
                member.getEmail(),
                member.getName(),
                member.getRole().name()
        );
    }
}
