package com.personal.movie.service;

import com.personal.movie.domain.Movie;
import com.personal.movie.dto.MovieDto;
import com.personal.movie.dto.response.MovieApiResponse;
import com.personal.movie.dto.response.MovieByPersonResponse;
import com.personal.movie.dto.response.MovieResultResponse;
import com.personal.movie.dto.response.PersonResponse;
import com.personal.movie.repository.MovieRepository;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalMovieService {

    private final MovieRepository movieRepository;
    private final WebClient webClient = WebClient.create();

    @Value("${movie.secret}")
    private String apiKey;


    public List<MovieDto> getPopularMovies() {
        int page = 50;
        List<MovieDto> storeMovies = new ArrayList<>();

        for (int i = 1; i <= page; i++) {
            String apiUrl =
                "https://api.themoviedb.org/3/movie/popular?language=ko&page=" + i + "&api_key="
                    + apiKey;
            MovieApiResponse movieApiResponse = webClient.get()
                .uri(apiUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(MovieApiResponse.class)
                .block();

            if (movieApiResponse != null) {
                storeMovies.addAll(saveMovies(movieApiResponse.getResults()));
            }
        }

        log.warn("저장 완료");
        return storeMovies;
    }


    public List<MovieDto> getMovieByKeyword(String keyword) throws IOException {
        // 원제목과 제목에 키워드가 포함된 영화들을 db 에서 조회
        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCaseOrOriginalTitleContainingIgnoreCase(
            keyword, keyword).orElse(Collections.emptyList());

        // 영화들이 db 에 존재한다면 가져오고, 존재하지 않는다면 TMDB API 를 사용해서 영화를 저장하고 반환
        if (!movies.isEmpty()) {
            return movies.stream().map(MovieDto::fromEntity).toList();
        } else {
            int currentPage = 1;
            int totalPages = Integer.MAX_VALUE;
            List<MovieDto> storeMovies = new ArrayList<>();

            while (currentPage <= totalPages) {
                String apiUrl =
                    "https://api.themoviedb.org/3/search/movie?query="
                        + keyword + "&language=ko&page=" + currentPage
                        + "&api_key="
                        + apiKey;

                MovieApiResponse movieApiResponse = webClient.get()
                    .uri(apiUrl)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(MovieApiResponse.class)
                    .block();

                if (movieApiResponse == null || isResultsEmpty(movieApiResponse.getResults())) {
                    break;
                }

                storeMovies.addAll(saveMovies(movieApiResponse.getResults()));

                currentPage++;
                totalPages = movieApiResponse.getTotalPages();
            }

            log.warn("저장 완료");

            return storeMovies;
        }
    }


    public List<MovieDto> getMovieByPerson(String personName) throws IOException {
        List<MovieDto> storeMovies = new ArrayList<>();

        int currentPage = 1;
        int totalPages = Integer.MAX_VALUE;

        while (currentPage <= totalPages) {
            String apiUrl =
                "https://api.themoviedb.org/3/search/person?query="
                    + personName
                    + "&language=ko&page=" + currentPage
                    + "&api_key="
                    + apiKey;

            MovieByPersonResponse movieByPersonResponse = webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(MovieByPersonResponse.class)
                .block();

            assert movieByPersonResponse != null;
            List<PersonResponse> personResponses = movieByPersonResponse.getResults();

            for (PersonResponse personResponse : personResponses) {
                if (isResultsEmpty(personResponse.getKnownFor())) {
                    break;
                }

                storeMovies.addAll(saveMovies(personResponse.getKnownFor()));
            }

            currentPage++;
            totalPages = movieByPersonResponse.getTotalPages();
        }
        log.warn("저장 완료");

        return storeMovies;
    }

    private boolean isResultsEmpty(List<MovieResultResponse> results) {
        boolean isEmpty = results == null || results.isEmpty();
        log.debug("Results are empty: {}", isEmpty);
        return isEmpty;
    }

    private List<Movie> resultToEntity(List<MovieResultResponse> movieResultList) {
        List<Movie> movies = movieResultList.stream()
            // DB에 저장하기 위해 장르를 정수형에서 문자열로 바꾸고 엔티티로 만들어줍니다.
            .map(MovieResultResponse::toEntity)
            .collect(Collectors.toList());

        return movies;
    }

    private List<MovieDto> saveMovies(List<MovieResultResponse> movieResultList) {
        List<Movie> movies = resultToEntity(movieResultList);
        List<Long> movieIds = movies.stream().map(Movie::getMovieId).collect(Collectors.toList());

        // DB에 이미 존재하는 영화들을 찾아옴
        List<Movie> existingMovies = movieRepository.findByMovieIdIn(movieIds);

        // 중복되지 않은 영화들만 저장
        List<MovieDto> storeMovies = new ArrayList<>();
        for (Movie movie : movies) {
            if (!containsMovieId(existingMovies, movie.getMovieId())) {
                movieRepository.save(movie);
                storeMovies.add(MovieDto.fromEntity(movie));
            }
        }

        return storeMovies;
    }

    private boolean containsMovieId(List<Movie> movies, Long movieId) {
        for (Movie existingMovie : movies) {
            if (existingMovie.getMovieId().equals(movieId)) {
                return true;
            }
        }
        return false;
    }
}
