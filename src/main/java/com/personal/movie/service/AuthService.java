package com.personal.movie.service;

import com.personal.movie.domain.Member;
import com.personal.movie.domain.constants.ErrorCode;
import com.personal.movie.domain.constants.Role;
import com.personal.movie.dto.TokenDto;
import com.personal.movie.dto.request.MailRequest;
import com.personal.movie.dto.request.MemberRequest;
import com.personal.movie.dto.request.TokenRequest;
import com.personal.movie.dto.response.MemberResponse;
import com.personal.movie.exception.CustomException;
import com.personal.movie.repository.MemberRepository;
import com.personal.movie.security.TokenProvider;
import com.personal.movie.util.RedisUtil;
import com.personal.movie.util.SecurityUtil;
import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisUtil redisUtil;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final RedisUtil redisConfig;

    private final long duration = 14 * 24 * 60 * 60;


    public MemberResponse signup(MemberRequest request) {
        validateMemberName(request);
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        request.setRole(Role.ROLE_USER);
        Member member = memberRepository.save(request.toEntity());

        return MemberResponse.fromEntity(member);
    }

    public TokenDto login(MemberRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = request.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject()
            .authenticate(authenticationToken);
        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        redisUtil.setDataExpire(authentication.getName(), tokenDto.getRefreshToken(),
            duration);

        return tokenDto;
    }

    public String logout(TokenDto tokenDto) {
        String accessToken = tokenDto.getAccessToken();

        if (!tokenProvider.validateToken(accessToken)) {
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        if (redisUtil.getData(authentication.getName()) != null) {
            redisUtil.deleteData(authentication.getName());
        }

        long remainingTime = TimeUnit.MILLISECONDS.toSeconds(
            tokenProvider.getRemainingTime(accessToken));

        if (remainingTime > 0) {
            redisUtil.setDataExpire(accessToken, "logout", remainingTime);
        }

        return authentication.getName() + " 님이 정상적으로 로그아웃 되었습니다.";
    }

    public TokenDto reissue(TokenRequest request) {

        if (!tokenProvider.validateToken(request.getRefreshToken())) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());

        if (redisUtil.getData(request.getRefreshToken()) == null) {
            throw new CustomException(ErrorCode.MEMBER_LOGGED_OUT);
        }

        TokenDto tokenDto = tokenProvider.generateToken(authentication);
        redisUtil.setDataExpire(authentication.getName(), tokenDto.getRefreshToken(), duration);

        return tokenDto;
    }

    private void validateMemberName(MemberRequest request) {
        if (memberRepository.existsByMemberName(request.getMemberName())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
        }
    }

    public String validateEmail(MailRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_EMAIL);
        }

        mailService.sendAuthEmail(request.getEmail());

        return "메일을 전송했습니다.";
    }

    public String validateAuthKey(String email, @NotBlank String key) {
        String authKey = redisConfig.getData(email);

        if (!key.equals(authKey)) {
            throw new CustomException(ErrorCode.AUTH_KEY_NOT_MATCH);
        }

        return "인증이 성공하였습니다.";
    }

}
