package com.personal.movie.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieApiResponse {

    @JsonProperty("page")
    private int page;

    @JsonProperty("results")
    private List<MovieResultResponse> results;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("total_results")
    private int totalResults;

}
