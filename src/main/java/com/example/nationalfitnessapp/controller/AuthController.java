package com.example.nationalfitnessapp.controller;

import com.example.nationalfitnessapp.domain.User;
import com.example.nationalfitnessapp.dto.LoginRequestDto;
import com.example.nationalfitnessapp.dto.SignupRequestDto;
import com.example.nationalfitnessapp.dto.TokenDto;
import com.example.nationalfitnessapp.dto.TokenRequestDto;
import com.example.nationalfitnessapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 회원가입 API
     * @param requestDto SignupRequestDto 형식으로 저장된 Dto
     * @return http response와 생성된 User 정보를 보낸다.
     * */
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignupRequestDto requestDto) {
        User savedUser = userService.signUp(
                requestDto.getLoginId(),
                requestDto.getPassword(),
                requestDto.getNickname(),
                requestDto.getEmail()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);  // CREATED라는 http response를 보내고 body에 저장된 user를 보낸다.
    }

    /**
     * 로그인 API
     * @param requestDto LoginRequestDto 형식으로 저장된 Dto
     * @return http response와 토큰 Dto를 반환한다.
     * */
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDto requestDto) {
        TokenDto tokenDto = userService.login(requestDto.getLoginId(), requestDto.getPassword());
        // 성공 시 Http 200 OK 상태 코드와 함께 DTO 반환
        return ResponseEntity.ok(tokenDto);
    }

    /**
     * 토큰 재발급 API
     * @param requestDto TokenRequestDto 형식으로 저장된 Dto
     * @return http response와 토큰 Dto를 반환한다.
     * */
    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody TokenRequestDto requestDto) {
        TokenDto newTokenDto = userService.refreshToken(requestDto.getAccessToken(), requestDto.getRefreshToken());
        return ResponseEntity.ok(newTokenDto);
    }
}
