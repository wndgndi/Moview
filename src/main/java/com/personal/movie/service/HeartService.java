package com.personal.movie.service;

import com.personal.movie.domain.Heart;
import com.personal.movie.domain.Member;
import com.personal.movie.domain.Post;
import com.personal.movie.domain.constants.ErrorCode;
import com.personal.movie.dto.HeartDto;
import com.personal.movie.exception.CustomException;
import com.personal.movie.repository.HeartRepository;
import com.personal.movie.repository.MemberRepository;
import com.personal.movie.repository.PostRepository;
import com.personal.movie.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public HeartDto heart(Long postId) {
        Member member = memberRepository.findByMemberName(SecurityUtil.getCurrentMemberName())
            .orElseThrow(() -> new CustomException(
                ErrorCode.MEMBER_NOT_FOUND));

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (heartRepository.existsByMember_IdAndPost_Id(member.getId(), postId)) {
            Heart heart = heartRepository.findByMember_IdAndPost_Id(member.getId(), postId)
                .orElseThrow(() -> new CustomException(ErrorCode.HEART_NOT_FOUND));

            heartRepository.delete(heart);
            return HeartDto.fromEntity(heart);
        }

        Heart heart = Heart.builder()
            .member(member)
            .post(post)
            .build();

        return HeartDto.fromEntity(heartRepository.save(heart));
    }
}
