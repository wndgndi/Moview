package com.personal.movie.config;

import com.personal.movie.security.JwtAccessDeniedHandler;
import com.personal.movie.security.JwtAuthenticationEntryPoint;
import com.personal.movie.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // @EnableWebSecurity 를 사용하면 Spring Security 를 내부적으로 구성하고 HttpSecurity 타입의 빈을 제공
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        Object SecurityConfigurerAdapter = null;
        http.httpBasic(HttpBasicConfigurer::disable)
            .csrf(CsrfConfigurer::disable)
            .cors(CorsConfigurer::disable)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((matcher) -> matcher.requestMatchers("/auth/**").permitAll()
                .requestMatchers("/member/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/movie/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/movie/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/review").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/review/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/review/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/post").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/post/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/post/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/post/my").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/comment/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/comment/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/comment/**").hasAnyRole("USER", "ADMIN")

                .anyRequest().authenticated())
            .exceptionHandling((exceptionHandling) ->
                exceptionHandling
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
