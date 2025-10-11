package com.example.nationalfitnessapp.dto;

import com.example.nationalfitnessapp.domain.DailyLog;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DailyLogResponseDto {
    private final Long logId;
    private final LocalDate date;
    private final String memo;
    private final List<DailyLogExerciseDto> exercises;

    public DailyLogResponseDto(DailyLog dailyLog) {
        this.logId = dailyLog.getLogId();
        this.date = dailyLog.getDate();
        this.memo = dailyLog.getMemo();
        this.exercises = dailyLog.getDailyLogExercises().stream()
                .map(DailyLogExerciseDto::new)
                .collect(Collectors.toList());
    }
}
