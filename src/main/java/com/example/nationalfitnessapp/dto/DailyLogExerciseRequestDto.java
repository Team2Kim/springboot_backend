package com.example.nationalfitnessapp.dto;

import lombok.Getter;

@Getter
public class DailyLogExerciseRequestDto {
    private Long exerciseId;
    private String intensity;
    private int exerciseTime;
}