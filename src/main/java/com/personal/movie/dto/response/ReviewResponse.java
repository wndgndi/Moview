package com.personal.movie.dto.response;

import com.personal.movie.domain.Image;
import com.personal.movie.domain.Review;
import com.personal.movie.domain.constants.Star;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReviewResponse {

    private String content;

    private Star star;

    private String memberName;

    private String movieTitle;

    private List<String> paths;

    public static ReviewResponse fromEntity(Review review) {
        return ReviewResponse.builder()
            .content(review.getContent())
            .star(review.getStar())
            .memberName(review.getMember().getMemberName())
            .movieTitle(review.getMovie().getTitle())
            .paths(review.getImages().stream().map(Image::getPath).toList())
            .build();
    }
}
