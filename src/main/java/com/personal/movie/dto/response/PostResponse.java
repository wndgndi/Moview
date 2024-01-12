package com.personal.movie.dto.response;

import com.personal.movie.domain.Image;
import com.personal.movie.domain.Post;
import com.personal.movie.domain.constants.Category;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostResponse {

    private String title;

    private String content;

    private Category category;

    private int viewCount;

    private int heartCount;

    private List<String> paths;

    public static PostResponse fromEntity(Post post) {
        return PostResponse.builder()
            .title(post.getTitle())
            .content(post.getContent())
            .category(post.getCategory())
            .viewCount(post.getViewCount())
            .heartCount(post.getHearts() == null ? 0 : post.getHearts().size())
            .paths(post.getImages().stream().map(Image::getPath).toList())
            .build();
    }
}
