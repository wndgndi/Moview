package com.personal.movie.repository;

import com.personal.movie.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByMember_IdAndMovie_Id(Long member_id, Long movie_id);
}
