package com.personal.movie.domain;

import com.personal.movie.dto.MovieDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean adult;   // 성인 영화 여부

    private String genre;  //  장르

    private String backdropPath;   // 배경

    @Column(unique = true)
    private Long movieId;   // 영화 id

    private String originalLanguage;

    private String originalTitle;   // 원제목

    @Column(columnDefinition = "TEXT")
    private String overview;   // 개요

    private double popularity;   // 인기도

    private String posterPath;   // 포스터

    private String releaseDate;   // 개봉일

    private String title;   // 제목

    private double voteAverage;   //  평점

    private int voteCount;  // 투표수

    @OneToMany(mappedBy = "movie")
    private List<History> histories;

    @OneToMany(mappedBy = "movie")
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "movie")
    private List<Review> reviews;

    public void updateMovie(MovieDto movieDto) {
        this.adult = movieDto.isAdult();
        this.genre = movieDto.getGenre();
        this.backdropPath = movieDto.getBackdropPath();
        this.movieId = movieDto.getMovieId();
        this.originalLanguage = movieDto.getOriginalLanguage();
        this.originalTitle = movieDto.getOriginalTitle();
        this.overview = movieDto.getOverview();
        this.popularity = movieDto.getPopularity();
        this.posterPath = movieDto.getPosterPath();
        this.releaseDate = movieDto.getReleaseDate();
        this.title = movieDto.getTitle();
        this.voteAverage = movieDto.getVoteAverage();
        this.voteCount = movieDto.getVoteCount();
    }
}