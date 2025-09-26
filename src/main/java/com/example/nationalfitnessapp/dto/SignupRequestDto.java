package com.example.nationalfitnessapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String loginId;
    private String password;
    private String nickname;
    private String email;
}
