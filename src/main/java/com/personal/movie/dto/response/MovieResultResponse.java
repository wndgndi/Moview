package com.personal.movie.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.personal.movie.domain.Movie;
import com.personal.movie.domain.constants.Genre;
import jakarta.persistence.Column;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
public class MovieResultResponse {

    @JsonProperty("adult")
    private boolean adult;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("id")
    private Long movieId;

    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("original_title")
    private String originalTitle;   // 원제목

    @JsonProperty("overview")
    @Column(columnDefinition = "TEXT")
    private String overview;   // 개요

    @JsonProperty("popularity")
    private double popularity;   // 인기도

    @JsonProperty("poster_path")
    private String posterPath;   // 포스터

    @JsonProperty("release_date")
    private String releaseDate;   // 개봉일

    @JsonProperty("title")
    private String title;   // 제목

    @JsonProperty("vote_average")
    private double voteAverage;   //  평점

    @JsonProperty("vote_count")
    private int voteCount;  // 투표수

    @JsonProperty("genre_ids")
    private List<Integer> genres;  //  장르

    public String mapGenreIdsToNames() {
        StringBuilder sb = new StringBuilder();

        if (genres != null) {
            sb.append(genres.stream()
                .map(Genre::nameOfId)
                .collect(Collectors.joining(",")));
        }
        return sb.toString();

    }

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
            .genre(mapGenreIdsToNames())
            .build();
    }
}
