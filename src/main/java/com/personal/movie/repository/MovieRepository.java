package com.personal.movie.repository;

import com.personal.movie.domain.Movie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<List<Movie>> findByTitleContainingIgnoreCaseOrOriginalTitleContainingIgnoreCase(
        String titleKeyword, String originalTitleKeyword);

    boolean existsByMovieId(Long movieId);

    Movie findByMovieId(Long movieId);

    List<Movie> findByMovieIdIn(List<Long> movieIds);
}
