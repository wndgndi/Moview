package com.personal.movie.controller;

import com.personal.movie.dto.request.MemberRequest;
import com.personal.movie.dto.response.MemberResponse;
import com.personal.movie.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @DeleteMapping("/{memberId}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.deleteMember(memberId));
    }


    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.getMember(memberId));
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long memberId,
        @RequestBody MemberRequest request) {
        return ResponseEntity.ok(memberService.updateMember(memberId, request));
    }
}
