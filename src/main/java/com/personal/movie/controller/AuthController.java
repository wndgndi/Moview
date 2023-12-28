package com.personal.movie.controller;

import com.personal.movie.dto.TokenDto;
import com.personal.movie.dto.request.MailRequest;
import com.personal.movie.dto.request.MemberRequest;
import com.personal.movie.dto.request.TokenRequest;
import com.personal.movie.dto.response.MemberResponse;
import com.personal.movie.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(@RequestBody MemberRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenDto tokenDto) {
        return ResponseEntity.ok(authService.logout(tokenDto) + " 님이 정상적으로 로그아웃 되었습니다.");
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(authService.reissue(request));
    }

    @PostMapping("/email")
    public ResponseEntity<String> validateEmail(@RequestBody MailRequest request) {
        authService.validateEmail(request);
        return ResponseEntity.ok("메일을 전송했습니다.");
    }

    @PostMapping("/key")
    public ResponseEntity<String> validateAuthKey(@RequestParam String email,
        @RequestParam String key) {
        authService.validateAuthKey(email, key);
        return ResponseEntity.ok("인증이 성공했습니다.");
    }
}
