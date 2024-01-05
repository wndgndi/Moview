package com.personal.movie.domain;

import com.personal.movie.domain.constants.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String memberName;   // 아이디

    private String password;   // 비밀번호

    private String name;   // 이름

    @Column(unique = true)
    private String email;   // 이메일

    private String phoneNumber;  // 전화번호

    @Enumerated(EnumType.STRING)
    private Role role;   // 권한

    @OneToMany(mappedBy = "member")
    private List<History> history;

    @OneToMany(mappedBy = "member")
    private List<Favorite> favorite;

    @Builder
    public Member(String memberName, String password, String name, String email,
        String phoneNumber, Role role) {
        this.memberName = memberName;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
