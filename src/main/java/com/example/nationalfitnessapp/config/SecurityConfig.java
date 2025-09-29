package com.example.nationalfitnessapp.config;

import com.example.nationalfitnessapp.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // 1. REST API는 세션을 사용하지 않고 JWT 토큰 방식을 사용하므로 CSRF 보호 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  //  2. 세션 비활성화 (JWT 기반 인증에서는 서버가 세션을 유지할 필요가 없음)
                .authorizeHttpRequests(auth -> auth  // 3. 특정 URL에 대한 접근 권한 설정
                        .requestMatchers("/api/auth/**").permitAll()  // 3-1. "/api/auth/**" 패턴의 URL 요청은 인증 없이 모두 허용
                        .requestMatchers("/api/exercises/**").permitAll()  // 3-2. "/api/exercises/**" 패턴의 URL 요청은 인증 없이 모두 허용
                        .requestMatchers("/api/facilities/**").permitAll()  // 3-3. "/api/facilities/**" 패턴의 URL 요청은 인증 없이 모두 허용
                        .requestMatchers("/api/bookmarks/**").permitAll()
                        .anyRequest().authenticated()  // 3-4. 그 외의 모든 요청은 반드시 인증을 거쳐야 함
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}