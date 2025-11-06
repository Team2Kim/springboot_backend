package com.example.nationalfitnessapp.domain;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DailyLogExercise")
@Getter
@Setter
@NoArgsConstructor
public class DailyLogExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logExerciseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logId", nullable = false)
    private DailyLog dailyLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exerciseId", nullable = false)
    private Exercise exercise;

    @Column(nullable = false, length=10)
    private String intensity;

    @Column(nullable = false)
    private int exerciseTime;

    // [추가] 개별 운동에 대한 메모 필드
    @Column(columnDefinition = "TEXT")
    private String exerciseMemo;

    public DailyLogExercise(DailyLog dailyLog, Exercise exercise, String intensity, int exerciseTime, String exerciseMemo){
        this.dailyLog = dailyLog;
        this.exercise = exercise;
        this.intensity = intensity;
        this.exerciseTime = exerciseTime;
        this.exerciseMemo = exerciseMemo;
    }
}
