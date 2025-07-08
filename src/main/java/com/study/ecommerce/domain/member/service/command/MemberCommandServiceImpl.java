package com.study.ecommerce.domain.member.service.command;

import com.study.ecommerce.domain.member.entity.Member;
import com.study.ecommerce.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberCommandServiceImpl implements MemberCommandService{
    private final MemberRepository memberRepository;

    @Override
    public Member createMember(String email, String name, String password) {
        return null;
    }

    @Override
    public Member createMember(String email, String name, String password, String address, Member.Role role) {
        return null;
    }

    @Override
    public Member updateMember(Long memberId, String name) {
        return null;
    }

    @Override
    public void deleteMember(Long memberId) {

    }
}
