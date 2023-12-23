package com.personal.movie.controller;

import com.personal.movie.dto.TokenDto;
import com.personal.movie.dto.request.MailRequest;
import com.personal.movie.dto.request.MemberRequest;
import com.personal.movie.dto.request.TokenRequest;
import com.personal.movie.dto.response.MemberResponse;
import com.personal.movie.service.AuthService;
import com.personal.movie.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final MailService mailService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(@RequestBody MemberRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @GetMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenDto tokenDto) {
        return ResponseEntity.ok(authService.logout(tokenDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(authService.reissue(request));
    }

    @GetMapping("/email")
    public ResponseEntity<String> validateEmail(@RequestBody MailRequest request) {
        return ResponseEntity.ok(authService.validateEmail(request));
    }

    @GetMapping("/key")
    public ResponseEntity<String> validateAuthKey(@RequestParam String email,
        @RequestParam String key) {
        return ResponseEntity.ok(authService.validateAuthKey(email, key));
    }
}
