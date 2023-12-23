package com.personal.movie.service;

import com.personal.movie.domain.Member;
import com.personal.movie.domain.constants.ErrorCode;
import com.personal.movie.dto.response.MemberResponse;
import com.personal.movie.exception.CustomException;
import com.personal.movie.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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
}
