package com.personal.movie.repository;

import com.personal.movie.domain.Favorite;
import com.personal.movie.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByMember_IdAndMovie_Id(Long member_id, Long movie_id);

    void deleteByMember_IdAndMovie_Id(Long member_id, Long movie_id);

    List<Favorite> findByMemberId(Long member_id);
}
