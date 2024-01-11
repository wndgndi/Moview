package com.personal.movie.service;

import com.personal.movie.domain.Member;
import com.personal.movie.domain.constants.ErrorCode;
import com.personal.movie.domain.constants.Role;
import com.personal.movie.dto.request.MemberRequest;
import com.personal.movie.dto.response.MemberResponse;
import com.personal.movie.exception.CustomException;
import com.personal.movie.repository.MemberRepository;
import com.personal.movie.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public MemberResponse deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        authService.checkAuthorization(member);

        memberRepository.deleteById(memberId);

        return MemberResponse.fromEntity(member);
    }

    public MemberResponse getMember(Long memberId) {
        return MemberResponse.fromEntity(memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)));
    }

    public MemberResponse updateMember(Long memberId, MemberRequest request) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        authService.checkAuthorization(member);

        String currentMemberName = SecurityUtil.getCurrentMemberName();
        Member currentMember = memberRepository.findByMemberName(currentMemberName)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getMemberName().equals(currentMemberName) && currentMember.getRole()
            != Role.ROLE_ADMIN && memberRepository.existsByEmail(
            request.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);
        }
        member.updateEmail(request.getEmail());
        member.updatePassword(passwordEncoder.encode(request.getPassword()));
        member.updateName(request.getName());
        member.updatePhoneNumber(request.getPhoneNumber());

        return MemberResponse.fromEntity(memberRepository.save(member));
    }
}
