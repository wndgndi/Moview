package com.personal.movie.dto.response;

import com.google.gson.annotations.SerializedName;
import com.personal.movie.domain.Movie;
import com.personal.movie.domain.constants.Genre;
import jakarta.persistence.Column;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class MovieResultResponse {

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("id")
    private Long movieId;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;   // 원제목

    @SerializedName("overview")
    @Column(columnDefinition = "TEXT")
    private String overview;   // 개요

    @SerializedName("popularity")
    private double popularity;   // 인기도

    @SerializedName("poster_path")
    private String posterPath;   // 포스터

    @SerializedName("release_date")
    private String releaseDate;   // 개봉일

    @SerializedName("title")
    private String title;   // 제목

    @SerializedName("vote_average")
    private double voteAverage;   //  평점

    @SerializedName("vote_count")
    private int voteCount;  // 투표수

    @SerializedName("genre_ids")
    private List<Integer> genres;  //  장르

    public String mapGenreIdsToNames() {
        StringBuilder sb = new StringBuilder();

        sb.append(genres.stream()
            .map(Genre::nameOfId)
            .collect(Collectors.joining(",")));
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
