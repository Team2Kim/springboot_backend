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
import org.springframework.web.bind.annotation.*;

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

    /**
     * 아이디 중복을 확인하는 API
     * @param loginId 중복을 확인하고 싶은 로그인 ID
     * @return 중복이 없는 경우 Ok, 중복인 경우 BadRequest 반환
     * */
    @GetMapping("/check-loginId")
    public ResponseEntity<Void> checkLoginId(@RequestParam String loginId) {
        if (userService.checkLoginIdDuplicated(loginId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * 닉네임 중복을 확인하는 API
     * @param nickname 중복을 확인하고 싶은 닉네임
     * @return 중복이 없는 경우 OK, 중복인 경우 BadRequest 반환
     * */
    @GetMapping("/check-nickname")
    public ResponseEntity<Void> checkNickname(@RequestParam String nickname) {
        if (userService.checkNicknameDuplicated(nickname)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * 이메일 중복을 확인하는 API
     * @param email 중복을 확인하고 싶은 이메일
     * @return 중복이 없는 경우 OK, 중복인 경우 BadRequest 반환
     * */
    @GetMapping("/check-email")
    public ResponseEntity<Void> checkEmail(@RequestParam String email) {
        if (userService.checkEmailDuplicated(email)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
