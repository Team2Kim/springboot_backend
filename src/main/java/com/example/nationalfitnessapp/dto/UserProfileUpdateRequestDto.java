package com.example.nationalfitnessapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // JSON 변환을 위해 기본 생성자가 필요
public class UserProfileUpdateRequestDto {

    private String targetGroup;
    private String fitnessLevelName;
    private String fitnessFactorName;
}
