package com.example.nationalfitnessapp.dto;

import com.example.nationalfitnessapp.domain.User;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class UserResponseDto {

    private final Long userId;
    private final String loginId;
    private final String nickname;
    private final String email;
    private final LocalDateTime createdAt;

    // 프로필 정보
    private final String targetGroup;
    private final String fitnessLevelName;
    private final String fitnessFactorName;

    // User 엔티티를 이 DTO로 변환하는 생성자
    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.loginId = user.getLoginId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.targetGroup = user.getTargetGroup();
        this.fitnessLevelName = user.getFitnessLevelName();
        this.fitnessFactorName = user.getFitnessFactorName();
    }
}