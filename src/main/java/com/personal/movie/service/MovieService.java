package com.personal.movie.service;

import com.google.gson.Gson;
import com.personal.movie.domain.History;
import com.personal.movie.domain.Member;
import com.personal.movie.domain.Movie;
import com.personal.movie.domain.constants.ErrorCode;
import com.personal.movie.dto.MovieDto;
import com.personal.movie.dto.response.MovieApiResponse;
import com.personal.movie.dto.response.MovieByPersonResponse;
import com.personal.movie.dto.response.MovieResultResponse;
import com.personal.movie.dto.response.PersonResponse;
import com.personal.movie.exception.CustomException;
import com.personal.movie.repository.HistoryRepository;
import com.personal.movie.repository.MemberRepository;
import com.personal.movie.repository.MovieRepository;
import com.personal.movie.util.SecurityUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
    private final Gson gson;

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

    public List<MovieDto> getPopularMovies(String apiKey) {
        int page = 50;
        List<MovieDto> storeMovies = new ArrayList<>();

        try {
            HttpURLConnection connection = null;
            BufferedReader br = null;
            for (int i = 1; i <= page; i++) {
                String apiUrl =
                    "https://api.themoviedb.org/3/movie/popular?language=ko&page=" + i + "&api_key="
                        + apiKey;
                URL url = new URL(apiUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                MovieApiResponse MovieApiResponse = new Gson().fromJson(response.toString(),
                    com.personal.movie.dto.response.MovieApiResponse.class);
                storeMovies.addAll(saveMovies(MovieApiResponse.getResults()));
            }
            br.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.warn("저장 완료");
        return storeMovies;
    }

    public List<MovieDto> getMovieByKeyword(String apiKey, String keyword) throws IOException {
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

            HttpURLConnection connection = null;
            BufferedReader br = null;
            do {
                try {
                    String apiUrl =
                        "https://api.themoviedb.org/3/search/movie?query=" + URLEncoder.encode(
                            keyword,
                            StandardCharsets.UTF_8) + "&language=ko&page=" + currentPage
                            + "&api_key="
                            + apiKey;
                    URL url = new URL(apiUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }

                    MovieApiResponse movieApiResponse = new Gson().fromJson(response.toString(),
                        MovieApiResponse.class);

                    if (isResultsEmpty(movieApiResponse.getResults())) {
                        break;
                    }

                    storeMovies.addAll(saveMovies(movieApiResponse.getResults()));

                    currentPage++;
                    totalPages = movieApiResponse.getTotalPages();
                } catch (IOException e) {
                    log.error("An error occurred", e);
                }
            } while (currentPage <= totalPages);
            br.close();
            connection.disconnect();
            log.warn("저장 완료");

            return storeMovies;

        }
    }

    public List<MovieDto> getMovieByPerson(String apiKey, String personName) throws IOException {
        List<MovieDto> storeMovies = new ArrayList<>();

        int currentPage = 1;
        int totalPages = Integer.MAX_VALUE;

        HttpURLConnection connection = null;
        BufferedReader br = null;
        do {
            try {
                String apiUrl =
                    "https://api.themoviedb.org/3/search/person?query=" + URLEncoder.encode(
                        personName,
                        StandardCharsets.UTF_8) + "&language=ko&page=" + currentPage
                        + "&api_key="
                        + apiKey;
                URL url = new URL(apiUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                MovieByPersonResponse movieByPersonResponse = new Gson().fromJson(
                    response.toString(),
                    MovieByPersonResponse.class);

                List<PersonResponse> personResponses = movieByPersonResponse.getResults();

                for (PersonResponse personResponse : personResponses) {
                    if (isResultsEmpty(personResponse.getKnownFor())) {
                        break;
                    }

                    // db 에 해당 영화가 존재하지 않으면 저장하고 리스트에 추가
                    // db 에 해당 영화가 존재하면 저장하지 않고 리스트에 추가
                    for (int i = 0; i < personResponse.getKnownFor().size(); i++) {
                        if (!movieRepository.existsByMovieId(
                            personResponse.getKnownFor().get(i).getMovieId())) {
                            storeMovies.add(MovieDto.fromEntity(movieRepository.save(
                                personResponse.getKnownFor().get(i).toEntity())));
                        } else {
                            storeMovies.add(MovieDto.fromEntity(
                                personResponse.getKnownFor().get(i).toEntity()));
                        }
                    }
                }

                currentPage++;
                totalPages = movieByPersonResponse.getTotalPages();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (currentPage <= totalPages);
        br.close();
        connection.disconnect();
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
            .map(MovieResultResponse::toEntity)
            .collect(Collectors.toList());

        return movies;
    }

    private List<MovieDto> saveMovies(List<MovieResultResponse> movieResultList) {
        List<Movie> movies = resultToEntity(movieResultList);
        List<MovieDto> storeMovies = new ArrayList<>();

        // DB 에 이미 존재하는 영화는 무시하고, 존재하지 않는 영화만 저장
        for (Movie movie : movies) {
            Movie existingMovie = movieRepository.findByMovieId(movie.getMovieId());

            if (existingMovie == null) {
                movieRepository.save(movie);
                storeMovies.add(MovieDto.fromEntity(movie));
            }
        }

        return storeMovies;
    }

}
