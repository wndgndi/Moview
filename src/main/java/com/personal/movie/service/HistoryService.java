package com.personal.movie.service;

import com.personal.movie.domain.History;
import com.personal.movie.domain.Member;
import com.personal.movie.domain.constants.ErrorCode;
import com.personal.movie.dto.MovieDto;
import com.personal.movie.exception.CustomException;
import com.personal.movie.repository.HistoryRepository;
import com.personal.movie.repository.MemberRepository;
import com.personal.movie.repository.MovieRepository;
import com.personal.movie.util.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final MovieRepository movieRepository;

    public List<MovieDto> getHistory() {
        Member member = memberRepository.findByMemberName(SecurityUtil.getCurrentMemberName())
            .orElseThrow(() -> new CustomException(
                ErrorCode.MEMBER_NOT_FOUND));

        List<MovieDto> movies = new ArrayList<>();
        List<History> histories = historyRepository.findByMemberOrderByUpdatedDate(member);

        for (int i = 0; i < histories.size(); i++) {
            History history = histories.get(i);
            MovieDto movie = MovieDto.fromEntity(
                movieRepository.findById(history.getMovie().getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND)));
            movies.add(movie);
        }

        return movies;
    }

    public void deleteHistory(Long historyId) {
        historyRepository.deleteById(historyId);
    }
}
