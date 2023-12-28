package com.personal.movie.service;

import com.google.gson.Gson;
import com.personal.movie.domain.Movie;
import com.personal.movie.dto.MovieDto;
import com.personal.movie.dto.response.MovieApiResponse;
import com.personal.movie.dto.response.MovieResultResponse;
import com.personal.movie.repository.MovieRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final Gson gson;

    public void getPopularMovie(String apiKey) {
        int page = 1;

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
                    MovieApiResponse.class);
                saveMovies(MovieApiResponse.getResults());
            }
            br.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.warn("저장 완료");
    }
    
    private void saveMovies(List<MovieResultResponse> movieResultList) {
        List<Movie> movies = movieResultList.stream()
            .map(MovieResultResponse::toEntity)
            .collect(Collectors.toList());

        movieRepository.saveAll(movies);
    }

}
