package com.personal.movie.service;

import com.personal.movie.domain.Image;
import com.personal.movie.domain.Member;
import com.personal.movie.domain.Movie;
import com.personal.movie.domain.Review;
import com.personal.movie.domain.constants.ErrorCode;
import com.personal.movie.domain.constants.Role;
import com.personal.movie.dto.request.ReviewRequest;
import com.personal.movie.dto.response.ReviewResponse;
import com.personal.movie.exception.CustomException;
import com.personal.movie.repository.ImageRepository;
import com.personal.movie.repository.MemberRepository;
import com.personal.movie.repository.MovieRepository;
import com.personal.movie.repository.ReviewRepository;
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
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ReviewService {

    private final MemberRepository memberRepository;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;

    @Value("${file.dir}")
    private String fileDir;

    public ReviewResponse createReview(ReviewRequest request, Long id,
        List<MultipartFile> multipartFiles) throws IOException {
        Member member = memberRepository.findByMemberName(SecurityUtil.getCurrentMemberName())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (reviewRepository.existsByMember_IdAndMovie_Id(member.getId(), id)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_REVIEW);
        }

        if (multipartFiles.size() > 5) {
            throw new CustomException(ErrorCode.TOO_MANY_IMAGES);
        }

        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));

        Review review = request.toEntity();
        review.setMember(member);
        review.setMovie(movie);
        reviewRepository.save(review);

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

            image.setReview(review);
            imageRepository.save(image);
            images.add(image);

            multipartFile.transferTo(file);
        }

        review.setImages(images);
        return ReviewResponse.fromEntity(review);
    }

    public ReviewResponse getReview(Long reviewId) {
        return ReviewResponse.fromEntity(reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND)));
    }

    public ReviewResponse updateReview(Long reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        Member currentMember = memberRepository.findByMemberName(
                SecurityUtil.getCurrentMemberName())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!currentMember.getMemberName().equals(review.getMember().getMemberName())
            && currentMember.getRole() != Role.ROLE_ADMIN) {
            throw new CustomException(ErrorCode.MEMBER_NOT_MATCH);
        }

        review.updateContent(request.getContent());
        review.updateStar(request.getStar());

        reviewRepository.save(review);
        return ReviewResponse.fromEntity(review);
    }

    public ReviewResponse deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        Member currentMember = memberRepository.findByMemberName(
                SecurityUtil.getCurrentMemberName())
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!currentMember.getMemberName().equals(review.getMember().getMemberName())
            && currentMember.getRole() != Role.ROLE_ADMIN) {
            throw new CustomException(ErrorCode.MEMBER_NOT_MATCH);
        }

        reviewRepository.delete(review);
        return ReviewResponse.fromEntity(review);
    }

}
