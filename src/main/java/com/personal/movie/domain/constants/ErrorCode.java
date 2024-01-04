package com.personal.movie.domain.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    MEMBER_NOT_FOUND(404, "회원을 찾을 수 없습니다."),
    MEMBER_NOT_MATCH(401, "회원이 일치하지 않습니다."),
    ALREADY_EXIST_MEMBER(409, "이미 존재하는 회원입니다."),
    ALREADY_EXIST_EMAIL(409, "이미 존재하는 이메일입니다."),
    AUTH_KEY_NOT_MATCH(404, "인증키가 일치하지 않습니다."),
    NO_AUTHORITIES(422, "권한 정보가 존재하지 않습니다."),
    EMPTY_SECURITY_CONTEXT(401, "Security Context 에 인증 정보가 없습니다."),
    AUTHORITY_MISMATCH(401, "본인 혹은 관리자만 사용 가능합니다."),
    INVALID_ACCESS_TOKEN(401, "유효하지 않은 Access Token 입니다."),
    INVALID_REFRESH_TOKEN(401, "유효하지 않은 Refresh Token 입니다."),
    MEMBER_LOGGED_OUT(401, "로그아웃 된 회원입니다."),
    MOVIE_NOT_FOUND(404, "영화를 찾을 수 없습니다."),
    ALREADY_EXIST_MOVIE(409, "이미 존재하는 영화 ID 입니다."),
    HISTORY_NOT_FOUND(404, "히스토리를 찾을 수 없습니다."),
    FAVORITE_NOT_FOUND(404, "관심영화를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "서버 에러가 발생했습니다. 고객센터로 문의 바랍니다.");


    private final int status;
    private final String description;
}
