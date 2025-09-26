package com.example.nationalfitnessapp.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDto {
    private String grantType;  // "Bearer
    private String accessToken;
    private String refreshToken;
}
