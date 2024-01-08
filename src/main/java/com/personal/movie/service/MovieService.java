package com.personal.movie.service;

import com.personal.movie.domain.History;
import com.personal.movie.domain.Member;
import com.personal.movie.domain.Movie;
import com.personal.movie.domain.constants.ErrorCode;
import com.personal.movie.dto.MovieDto;
import com.personal.movie.exception.CustomException;
import com.personal.movie.repository.HistoryRepository;
import com.personal.movie.repository.MemberRepository;
import com.personal.movie.repository.MovieRepository;
import com.personal.movie.util.SecurityUtil;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;
    private final HistoryRepository historyRepository;


    public MovieDto insertMovie(MovieDto movieDto) {
        if (movieRepository.existsByMovieId(movieDto.getMovieId())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_MOVIE);
        }
        movieRepository.save(movieDto.toEntity());
        return movieDto;
    }

    public MovieDto getMovie(Long id) {
        Member member = memberRepository.findByMemberName(SecurityUtil.getCurrentMemberName())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        // 이미 히스토리에 존재하는 영화일 경우, 수정날짜만 최신화해서 가장 최근 조회한 영화 조회 가능하도록 함
        if (historyRepository.existsByMember_IdAndMovie_Id(member.getId(), id)) {
            History history = historyRepository.findByMember_IdAndMovie_Id(member.getId(), id)
                .orElseThrow(() -> new CustomException(ErrorCode.HISTORY_NOT_FOUND));

            history.setUpdatedDate(LocalDateTime.now());
            historyRepository.save(history);

            return MovieDto.fromEntity(movie);
        }

        List<History> histories = historyRepository.findByMemberOrderByUpdatedDate(member);

        // 회원의 히스토리 내역이 10개를 초과하면 가장 오래된 히스토리를 삭제
        if (histories.size() >= 10) {
            History oldestHistory = histories.get(0);
            historyRepository.delete(oldestHistory);
        }

        History history = new History();
        history.setMember(member);
        history.setMovie(movie);
        historyRepository.save(history);

        return MovieDto.fromEntity(movie);
    }

    public MovieDto updateMovie(Long id, MovieDto movieDto) {
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        movie.updateMovie(movieDto);

        return MovieDto.fromEntity(movieRepository.save(movie));
    }

    public MovieDto deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));
        movieRepository.deleteById(id);

        return MovieDto.fromEntity(movie);
    }

}
