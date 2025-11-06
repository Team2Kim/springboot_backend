package com.example.nationalfitnessapp.dto;

import com.example.nationalfitnessapp.domain.DailyLogExercise;
import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class DailyLogExerciseDto {

    private final Long logExerciseId;
    private final ExerciseResponseDto exercise;
    private final String intensity;
    private final int exerciseTime;
    private String exerciseMemo;

    public DailyLogExerciseDto(DailyLogExercise dailyLogExercise) {
        this.logExerciseId = dailyLogExercise.getLogExerciseId();
        this.exercise = new ExerciseResponseDto(dailyLogExercise.getExercise());
        this.intensity = dailyLogExercise.getIntensity();
        this.exerciseTime = dailyLogExercise.getExerciseTime();
        this.exerciseMemo = dailyLogExercise.getExerciseMemo();
    }
}
