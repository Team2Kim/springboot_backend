package com.example.nationalfitnessapp.repository;

import com.example.nationalfitnessapp.domain.DailyLog;
import com.example.nationalfitnessapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyLogRepository extends JpaRepository<DailyLog, Long>{

    // 특정 사용자의 특정 날짜 일지를 조회
    Optional<DailyLog> findByUserAndDate(User user, LocalDate date);

    // 이 쿼리 의미는?
    @Query("SELECT dl FROM DailyLog dl " +
    "JOIN FETCH dl.user " +
    "LEFT JOIN FETCH dl.dailyLogExercises dle " +
    "LEFT JOIN FETCH dle.exercise " +
    "WHERE dl.logId = :logId")
    Optional<DailyLog> findByIdWithDetails(@Param("logId") Long logId);
}