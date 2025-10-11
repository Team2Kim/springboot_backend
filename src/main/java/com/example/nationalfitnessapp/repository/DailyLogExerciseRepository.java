package com.example.nationalfitnessapp.repository;

import com.example.nationalfitnessapp.domain.DailyLogExercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyLogExerciseRepository extends JpaRepository<DailyLogExercise, Long> {
}