package com.personal.movie.dto.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieByPersonResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<PersonResponse> results;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("total_results")
    private int totalResults;
}
