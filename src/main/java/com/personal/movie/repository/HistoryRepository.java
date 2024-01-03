package com.personal.movie.repository;

import com.personal.movie.domain.History;
import com.personal.movie.domain.Member;
import com.personal.movie.domain.Movie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findByMemberOrderByUpdatedDate(Member member);

    boolean existsByMovie_Id(Long id);

    Optional<History> findByMovie_Id(Long id);
}
