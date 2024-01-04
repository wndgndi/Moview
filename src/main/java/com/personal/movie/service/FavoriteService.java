package com.personal.movie.service;

import com.personal.movie.domain.Favorite;
import com.personal.movie.domain.Member;
import com.personal.movie.domain.Movie;
import com.personal.movie.domain.constants.ErrorCode;
import com.personal.movie.dto.MovieDto;
import com.personal.movie.exception.CustomException;
import com.personal.movie.repository.FavoriteRepository;
import com.personal.movie.repository.MemberRepository;
import com.personal.movie.repository.MovieRepository;
import com.personal.movie.util.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MovieRepository movieRepository;
    private final MemberRepository memberRepository;

    public MovieDto insertFavorite(Long id) {

        Member member = memberRepository.findByMemberName(SecurityUtil.getCurrentMemberName())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        if (favoriteRepository.existsByMember_IdAndMovie_Id(member.getId(), id)) {
            favoriteRepository.deleteByMember_IdAndMovie_Id(member.getId(), id);

            return MovieDto.fromEntity(movie);
        }

        Favorite favorite = new Favorite();
        favorite.setMember(member);
        favorite.setMovie(movie);
        favoriteRepository.save(favorite);

        return MovieDto.fromEntity(movie);
    }

    public List<MovieDto> getFavorites() {
        String memberName = SecurityUtil.getCurrentMemberName();

        Member member = memberRepository.findByMemberName(memberName)
            .orElseThrow(() -> new CustomException(
                ErrorCode.MEMBER_NOT_FOUND));

        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());

        return favorites.stream()
            .map(favorite -> MovieDto.fromEntity(favorite.getMovie()))
            .collect(Collectors.toList());
    }
}
