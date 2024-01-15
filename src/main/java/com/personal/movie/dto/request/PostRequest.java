package com.personal.movie.dto.request;

import com.personal.movie.domain.Post;
import com.personal.movie.domain.constants.Category;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostRequest {

    private String title;

    @Size(max = 1000, message = "게시글 내용은 1000자 이내로 작성해야 합니다.")
    private String content;

    private Category category;

    public Post toEntity() {
        return Post.builder()
            .title(title)
            .content(content)
            .category(category)
            .build();
    }
}
