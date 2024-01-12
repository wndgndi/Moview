package com.personal.movie.dto;

import com.personal.movie.domain.Comment;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentDto {

    private String memberName;

    private String content;

    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
            .memberName(comment.getMember().getMemberName())
            .content(comment.getContent())
            .build();
    }
}
