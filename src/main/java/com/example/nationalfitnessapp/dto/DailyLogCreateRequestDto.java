package com.example.nationalfitnessapp.dto;

import lombok.Getter;
import java.time.LocalDate;

@Getter
public class DailyLogCreateRequestDto {
    private LocalDate date;
    private String memo;
}
