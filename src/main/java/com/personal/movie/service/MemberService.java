package com.personal.movie.service;

import com.personal.movie.domain.Member;
import com.personal.movie.domain.constants.ErrorCode;
import com.personal.movie.dto.request.MemberRequest;
import com.personal.movie.dto.response.MemberResponse;
import com.personal.movie.exception.CustomException;
import com.personal.movie.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthService authService;

    public MemberResponse deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
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

        if (memberRepository.existsByMemberName(request.getMemberName())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_MEMBER);
        }
        member.updateMemberName(request.getMemberName());

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);
        }
        member.updateEmail(request.getEmail());
        member.updatePassword(request.getPassword());
        member.updateName(request.getName());
        member.updatePhoneNumber(request.getPhoneNumber());

        return MemberResponse.fromEntity(memberRepository.save(member));
    }
}
