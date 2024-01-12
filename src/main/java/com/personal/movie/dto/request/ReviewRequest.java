package com.personal.movie.dto.request;

import com.personal.movie.domain.Review;
import com.personal.movie.domain.constants.Star;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class ReviewRequest {

    @Size(max = 300, message = "리뷰 내용은 300자 이내로 작성해야 합니다.")
    private String content;

    private Star star;

    private List<MultipartFile> images;

    public Review toEntity() {
        return Review.builder()
            .content(content)
            .star(star)
            .build();
    }
}

