package com.personal.movie.dto;

import com.personal.movie.domain.Movie;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class MovieDto {

    private boolean adult;   // 성인 영화 여부

    private String genre;  //  장르

    private String backdropPath;   // 배경

    private Long movieId;   // 영화 id

    private String originalLanguage;

    private String originalTitle;   // 원제목

    private String overview;   // 개요

    private double popularity;   // 인기도

    private String posterPath;   // 포스터

    private String releaseDate;   // 개봉일

    private String title;   // 제목

    private double voteAverage;   //  평점

    private int voteCount;  // 투표수


    public Movie toEntity() {
        return Movie.builder()
            .adult(adult)
            .backdropPath(backdropPath)
            .movieId(movieId)
            .originalLanguage(originalLanguage)
            .originalTitle(originalTitle)
            .overview(overview)
            .popularity(popularity)
            .posterPath(posterPath)
            .releaseDate(releaseDate)
            .title(title)
            .voteAverage(voteAverage)
            .voteCount(voteCount)
            .genre(genre)
            .build();
    }

    public static MovieDto fromEntity(Movie movie) {
        return MovieDto.builder()
            .adult(movie.isAdult())
            .backdropPath(movie.getBackdropPath())
            .movieId(movie.getMovieId())
            .originalLanguage(movie.getOriginalLanguage())
            .originalTitle(movie.getOriginalTitle())
            .overview(movie.getOverview())
            .popularity(movie.getPopularity())
            .posterPath(movie.getPosterPath())
            .releaseDate(movie.getReleaseDate())
            .title(movie.getTitle())
            .voteAverage(movie.getVoteAverage())
            .voteCount(movie.getVoteCount())
            .genre(movie.getGenre())
            .build();
    }
}
