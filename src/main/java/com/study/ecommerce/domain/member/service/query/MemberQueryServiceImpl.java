package com.study.ecommerce.domain.member.service.query;

import com.study.ecommerce.domain.member.entity.Member;
import com.study.ecommerce.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryServiceImpl implements MemberQueryService{
    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> findById(Long memberId) {
        return Optional.empty();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long memberId) {
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }
}
