package com.dopaming.backend.global.config;

import com.dopaming.backend.global.filter.JwtAuthenticationFilter;
import com.dopaming.backend.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    // 비밀번호 암호화를 위한 Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // REST API와 JWT 인증을 사용하므로 CSRF, 폼 로그인, 기본 인증을 비활성화한다.
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // 프론트엔드 배포 도메인과 로컬 개발 환경에서 API 호출이 가능하도록 CORS 설정을 적용한다.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // JWT 기반 인증에서는 서버 세션을 사용하지 않는다.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // API 경로별 접근 권한을 설정한다.
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 접근 가능한 API
                        .requestMatchers(
                                // 로그인, 회원가입, 중복확인, 토큰 재발급 접근 허용
                                "/api/auth/signup",
                                "/api/auth/login",
                                "/api/auth/check-id",
                                "/api/auth/refresh",

                                // Swagger 문서 접근 허용
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // CORS preflight 요청 허용
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                        // 그 외 모든 요청은 JWT 인증이 필요하다.
                        .anyRequest().authenticated()
                )

                // JWT 인증 필터를 Spring Security의 기본 인증 필터 앞에 등록한다.
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 프론트엔드 로컬 개발 주소와 배포 주소를 허용한다.
        // 프론트 배포 후 실제 도메인을 아래 목록에 추가해야 한다.
        configuration.setAllowedOrigins(List.of(
                "http://localhost:8081",
                "https://프론트엔드-배포주소.com" // 배포 후 수정
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "PATCH",
                "DELETE",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));

        // Authorization 헤더 기반 JWT 방식에서는 필수는 아니지만,
        // 추후 인증 관련 헤더 처리를 고려해 허용한다.
        configuration.setAllowCredentials(true);

        // Preflight 요청 결과를 1시간 동안 캐싱한다.
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}