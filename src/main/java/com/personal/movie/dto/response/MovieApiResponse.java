package com.personal.movie.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieApiResponse {

    private int page;
    private List<MovieResultResponse> results;

    private int totalPages;

    private int totalResults;
}
