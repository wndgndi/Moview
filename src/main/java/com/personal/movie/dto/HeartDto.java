package com.personal.movie.dto;

import com.personal.movie.domain.Heart;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HeartDto {

    private Long memberId;
    private Long postId;

    public static HeartDto fromEntity(Heart heart) {
        return HeartDto.builder()
            .memberId(heart.getMember().getId())
            .postId(heart.getPost().getId())
            .build();
    }
}
