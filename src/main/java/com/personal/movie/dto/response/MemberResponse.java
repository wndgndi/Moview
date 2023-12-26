package com.personal.movie.dto.response;

import com.personal.movie.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponse {

    private String memberName;   // 아이디
    private String name;   // 이름
    private String email;   // 이메일
    private String phoneNumber;  // 전화번호


    public static MemberResponse fromEntity(Member member) {
        return MemberResponse.builder()
            .memberName(member.getMemberName())
            .name(member.getName())
            .email(member.getEmail())
            .phoneNumber(member.getPhoneNumber())
            .build();
    }
}
