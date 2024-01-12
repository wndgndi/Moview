package com.personal.movie.service;

import com.personal.movie.component.RedisComponent;
import com.personal.movie.domain.Image;
import com.personal.movie.domain.Member;
import com.personal.movie.domain.Post;
import com.personal.movie.domain.constants.Category;
import com.personal.movie.domain.constants.ErrorCode;
import com.personal.movie.domain.constants.Role;
import com.personal.movie.dto.request.PostRequest;
import com.personal.movie.dto.response.PostResponse;
import com.personal.movie.exception.CustomException;
import com.personal.movie.repository.ImageRepository;
import com.personal.movie.repository.MemberRepository;
import com.personal.movie.repository.PostRepository;
import com.personal.movie.util.SecurityUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final RedisComponent redisComponent;

    @Value("${file.dir}")
    private String fileDir;

    public PostResponse createPost(PostRequest request, List<MultipartFile> multipartFiles)
        throws IOException {
        Member member = memberRepository.findByMemberName(SecurityUtil.getCurrentMemberName())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (multipartFiles.size() > 5) {
            throw new CustomException(ErrorCode.TOO_MANY_IMAGES);
        }

        Post post = request.toEntity();
        post.setMember(member);
        postRepository.save(post);

        List<Image> images = new ArrayList<>();

        log.info("multiPartFile : {}", (Object) multipartFiles);
        for (MultipartFile multipartFile : multipartFiles) {
            String originalName = multipartFile.getOriginalFilename();
            log.info("originalName : {}", originalName);

            long size = multipartFile.getSize();
            log.info("size : {}", size);

            if (size == 0) {
                break;
            }

            String contentType = multipartFile.getContentType();
            log.info("contentType : {}", contentType);

            UUID uuid = UUID.randomUUID();
            String newFileName = uuid + "_" + originalName;

            String path = fileDir + File.separator + newFileName;
            log.info("fullPath : {}", path);

            File file = new File(path);

            if (!file.exists()) {
                file.mkdirs();
            }

            Image image = Image.builder()
                .uploadFileName(originalName)
                .storedFileName(newFileName)
                .path(path)
                .fileSize(size)
                .extension(contentType)
                .build();

            image.setPost(post);
            imageRepository.save(image);
            images.add(image);

            multipartFile.transferTo(file);
        }

        post.setImages(images);
        return PostResponse.fromEntity(post);
    }

    public PostResponse getPost(Long postId) {
        String memberName = SecurityUtil.getCurrentMemberName();
        String postKey = postId.toString();

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        int viewCount = post.getViewCount();

        if (!redisComponent.isExistData(memberName, postKey)) {
            redisComponent.setDataSetExpire(memberName, postKey);
            post.updateViewCount(++viewCount);
            postRepository.save(post);
        }

        return PostResponse.fromEntity(postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND)));
    }

    public PostResponse updatePost(Long postId, PostRequest request) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Member currentMember = memberRepository.findByMemberName(
                SecurityUtil.getCurrentMemberName())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!currentMember.getMemberName().equals(post.getMember().getMemberName())
            && currentMember.getRole() != Role.ROLE_ADMIN) {
            throw new CustomException(ErrorCode.MEMBER_NOT_MATCH);
        }

        post.updateTitle(request.getTitle());
        post.updateContent(request.getContent());
        post.updateCategory(request.getCategory());

        postRepository.save(post);
        return PostResponse.fromEntity(post);
    }

    public PostResponse deletePost(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Member currentMember = memberRepository.findByMemberName(
                SecurityUtil.getCurrentMemberName())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!currentMember.getMemberName().equals(post.getMember().getMemberName())
            && currentMember.getRole() != (Role.ROLE_ADMIN)) {
            throw new CustomException(ErrorCode.MEMBER_NOT_MATCH);
        }

        postRepository.delete(post);
        return PostResponse.fromEntity(post);
    }

    public List<PostResponse> getMyPosts() {
        return postRepository.findByMember_MemberName(SecurityUtil.getCurrentMemberName()).stream()
            .map(PostResponse::fromEntity).toList();
    }


    public List<PostResponse> searchByTitle(String keyword) {
        return postRepository.findByTitleContainingIgnoreCase(keyword).stream()
            .map(PostResponse::fromEntity).toList();
    }

    public List<PostResponse> searchByContent(String keyword) {
        return postRepository.findByContentContainingIgnoreCase(keyword).stream()
            .map(PostResponse::fromEntity).toList();
    }

    public List<PostResponse> searchByWriter(String memberName) {
        return postRepository.findByMember_MemberName(memberName).stream()
            .map(PostResponse::fromEntity).toList();
    }

    public List<PostResponse> searchByCategory(Category category) {

        return postRepository.findByCategory(category).stream()
            .map(PostResponse::fromEntity).toList();
    }

}