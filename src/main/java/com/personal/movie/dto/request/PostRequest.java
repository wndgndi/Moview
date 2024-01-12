package com.personal.movie.dto.request;

import com.personal.movie.domain.Post;
import com.personal.movie.domain.constants.Category;
import lombok.Getter;

@Getter
public class PostRequest {

    private String title;

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
