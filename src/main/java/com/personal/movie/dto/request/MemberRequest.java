package com.personal.movie.dto.request;

import com.personal.movie.domain.Member;
import com.personal.movie.domain.constants.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Setter
public class MemberRequest {

    private String memberName;   // 아이디
    private String password;   // 비밀번호
    private String name;   // 이름
    private String email;   // 이메일
    private String phoneNumber;  // 전화번호

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member toEntity() {
        return Member.builder()
            .memberName(memberName)
            .password(password)
            .name(name)
            .email(email)
            .phoneNumber(phoneNumber)
            .role(role)
            .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(memberName, password);
    }
}
