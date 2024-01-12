package com.personal.movie.service;

import com.personal.movie.domain.Comment;
import com.personal.movie.domain.Member;
import com.personal.movie.domain.Post;
import com.personal.movie.domain.constants.ErrorCode;
import com.personal.movie.domain.constants.Role;
import com.personal.movie.dto.CommentDto;
import com.personal.movie.exception.CustomException;
import com.personal.movie.repository.CommentRepository;
import com.personal.movie.repository.MemberRepository;
import com.personal.movie.repository.PostRepository;
import com.personal.movie.util.SecurityUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentDto createComment(CommentDto request, Long postId) {
        Member member = memberRepository.findByMemberName(SecurityUtil.getCurrentMemberName())
            .orElseThrow(() -> new CustomException(
                ErrorCode.MEMBER_NOT_MATCH));

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
            .content(request.getContent())
            .member(member)
            .post(post)
            .build();

        return CommentDto.fromEntity(commentRepository.save(comment));
    }

    public List<CommentDto> getComments(Long postId) {
        return commentRepository.findByPost_Id(postId).stream().map(CommentDto::fromEntity)
            .toList();
    }

    public CommentDto updateComment(CommentDto request, Long commentId) {
        Member currentMember = memberRepository.findByMemberName(
                SecurityUtil.getCurrentMemberName())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!currentMember.getMemberName().equals(comment.getMember().getMemberName())
            && currentMember.getRole() != Role.ROLE_ADMIN) {
            throw new CustomException(ErrorCode.MEMBER_NOT_MATCH);
        }

        comment.updateContent(request.getContent());

        return CommentDto.fromEntity(commentRepository.save(comment));
    }

    public CommentDto deleteComment(Long commentId) {
        Member currentMember = memberRepository.findByMemberName(
                SecurityUtil.getCurrentMemberName())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!currentMember.getMemberName().equals(comment.getMember().getMemberName())
            && currentMember.getRole() != Role.ROLE_ADMIN) {
            throw new CustomException(ErrorCode.MEMBER_NOT_MATCH);
        }

        commentRepository.delete(comment);
        return CommentDto.fromEntity(comment);
    }
}
