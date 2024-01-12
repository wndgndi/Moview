package com.personal.movie.repository;

import com.personal.movie.domain.Heart;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {

    Optional<Heart> findByMember_IdAndPost_Id(Long memberId, Long postId);

    boolean existsByMember_IdAndPost_Id(Long memberId, Long postId);

    List<Heart> findByPost_Id(Long postId);
}
