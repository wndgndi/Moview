package com.personal.movie.repository;

import com.personal.movie.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByMemberName(String memberName);

    boolean existsByEmail(String email);

    Optional<Member> findByMemberName(String memberName);

}
