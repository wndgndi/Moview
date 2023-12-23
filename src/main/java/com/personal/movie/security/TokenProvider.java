package com.personal.movie.security;

import com.personal.movie.domain.constants.ErrorCode;
import com.personal.movie.dto.TokenDto;
import com.personal.movie.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenProvider {

    private static final String KEY_ROLES = "role";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;

    private final Key key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(
            secretKey);  // Base64로 인코딩 된 secretKey 를 디코딩하여 keyBytes 배열에 저장 (시크릿 키를 바이트 배열로 변환)
        this.key = Keys.hmacShaKeyFor(
            keyBytes);    // JWT 를 서명할 때 사용되고, 서명된 토큰을 검증할 때 필요한  HMAC SHA 알고리즘을 적용한 key 를 생성
    }

    public TokenDto generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = new Date().getTime();
        Date accessTokenExpireIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
            .setSubject(authentication.getName())
            .claim(KEY_ROLES, authorities)
            .setIssuedAt(new Date())
            .setExpiration(accessTokenExpireIn)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();

        String refreshToken = Jwts.builder()
            .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();

        return TokenDto.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .accessTokenExpiresIn(accessTokenExpireIn.getTime())
            .refreshToken(refreshToken)
            .build();
    }

    public long getRemainingTime(String accessToken) {
        Claims claims = parseClaims(accessToken);
        Date expiration = claims.getExpiration();
        long currentTimeMills = System.currentTimeMillis();

        return expiration.getTime() - currentTimeMills;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(KEY_ROLES) == null) {
            throw new CustomException(ErrorCode.NO_AUTHORITIES);
        }

        Collection<? extends GrantedAuthority> roles =
            Arrays.stream(claims.get(KEY_ROLES).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails principal = new User(claims.getSubject(), "", roles);

        return new UsernamePasswordAuthenticationToken(principal, "", roles);
    }

    public String getMemberName(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }

        return false;
    }

    public Claims parseClaims(String accessToken) {
        try {
            // JWT(액세스 토큰)를 파싱하고, 해당 토큰의 서명을 검증한 후에 토큰의 본문을 반환
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
