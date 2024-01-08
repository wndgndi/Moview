package com.personal.movie.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonResponse {

    @JsonProperty("id")
    private Long PersonId;

    @JsonProperty("known_for_department")
    private String knownForDepartment;

    @JsonProperty("name")
    private String name;

    @JsonProperty("popularity")
    private double popularity;

    @JsonProperty("known_for")
    private List<MovieResultResponse> knownFor;
}
