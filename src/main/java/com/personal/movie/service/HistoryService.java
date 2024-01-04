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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final MovieRepository movieRepository;

    public List<MovieDto> getHistory() {
        Member member = memberRepository.findByMemberName(SecurityUtil.getCurrentMemberName())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<History> histories = historyRepository.findByMemberOrderByUpdatedDate(member);

        return histories.stream().map(history -> MovieDto.fromEntity(history.getMovie()))
            .collect(Collectors.toList());
    }

    public void deleteHistory(Long historyId) {
        Member member = memberRepository.findByMemberName(SecurityUtil.getCurrentMemberName())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        History history = historyRepository.findById(historyId)
            .orElseThrow(() -> new CustomException(ErrorCode.HISTORY_NOT_FOUND));

        if (history.getMember().equals(member)) {
            historyRepository.delete(history);
        } else {
            throw new CustomException(ErrorCode.MEMBER_NOT_MATCH);
        }

    }
}
