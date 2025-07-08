package com.study.ecommerce.domain.member.service.query;

import com.study.ecommerce.domain.member.entity.Member;

import java.util.Optional;

public interface MemberQueryService {
    Optional<Member> findById(Long memberId);
    Optional<Member> findByEmail(String email);

    boolean existsById(Long memberId);
    boolean existsByEmail(String email);
}
