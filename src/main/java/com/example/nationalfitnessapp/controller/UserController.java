package com.example.nationalfitnessapp.controller;

import com.example.nationalfitnessapp.domain.User;
import com.example.nationalfitnessapp.dto.UserProfileUpdateRequestDto;
import com.example.nationalfitnessapp.dto.UserResponseDto;
import com.example.nationalfitnessapp.security.UserDetailsImpl;
import com.example.nationalfitnessapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userservice;

    /**
     * 현재 로그인한 사용자의 프로필 정보를 수정하는 API
     */
    @PutMapping("/profile")
    public ResponseEntity<User> updateMyProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody UserProfileUpdateRequestDto requestDto
    ) {
        Long currentUserId = userDetails.getUser().getUserId();

        // 1. 서비스 로직을 호출하여 업데이트 수행
        userservice.updateUserProfile(currentUserId, requestDto);

        // 2. 성공했다는 의미로 200 OK 상태 코드와 빈 body를 반환
        return ResponseEntity.ok().build();
    }

    /**
     * 현재 로그인한 사용자의 프로필 정보를 조회하는 API
     */
    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getMyProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long currentUserId = userDetails.getUser().getUserId();
        UserResponseDto userProfile = userservice.getUserProfile(currentUserId);
        return ResponseEntity.ok(userProfile);
    }
}
