package com.study.ecommerce.domain.member.service.command;

import com.study.ecommerce.domain.member.entity.Member;

/**
 * Member 도메인의 Command
 * 회원 정보의 생성, 수정, 삭제
 */
public interface MemberCommandService {

    Member createMember(String email, String name, String password);

    Member createMember(String email, String name, String password, String address, Member.Role role);

    Member updateMember(Long memberId, String name);

    void deleteMember(Long memberId);
}
