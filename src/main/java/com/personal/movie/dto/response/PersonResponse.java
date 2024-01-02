package com.personal.movie.dto.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonResponse {

    @SerializedName("id")
    private Long PersonId;

    @SerializedName("known_for_department")
    private String knownForDepartment;

    @SerializedName("name")
    private String name;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("known_for")
    private List<MovieResultResponse> knownFor;
}
