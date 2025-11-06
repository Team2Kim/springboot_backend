package com.example.nationalfitnessapp.controller;

import com.example.nationalfitnessapp.domain.embed.AIFeedback;
import com.example.nationalfitnessapp.dto.*;
import com.example.nationalfitnessapp.security.UserDetailsImpl;
import com.example.nationalfitnessapp.service.DailyLogService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/journals")
@RequiredArgsConstructor
public class DailyLogController {

    private final DailyLogService dailyLogService;

    /**
     * 빈 운동 일지를 생성하는 API
     * @return 추가된 빈 운동 일지 리턴
     */
    @PostMapping
    public ResponseEntity<DailyLogResponseDto> createLog(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody DailyLogCreateRequestDto requestDto
    ) {
        Long userId = userDetails.getUser().getUserId();;
        DailyLogResponseDto createdLog = dailyLogService.createLog(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLog);
    }

    /**
     * 특정 일지의 메모를 수정하는 API
     */
    @PatchMapping("/{logId}")
    public ResponseEntity<Void> updateLogMemo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long logId,
            @RequestBody DailyLogUpdateRequestDto requestDto) {

        dailyLogService.updateLogMemo(userDetails.getUser().getUserId(), logId, requestDto.getMemo());
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 일지를 삭제하는 API
     */
    @DeleteMapping("/{logId}")
    public ResponseEntity<Void> deleteLog(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long logId) {

        dailyLogService.deleteLog(userDetails.getUser().getUserId(), logId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * 특정 일지에 운동 기록을 하나 추가하는 API
     * @param logId 운동을 추가할 일지의 ID
     */
    @PostMapping("/{logId}/exercises")
    public ResponseEntity<DailyLogResponseDto> addExerciseToLog(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long logId,
            @RequestBody DailyLogExerciseRequestDto requestDto
    ) {
        DailyLogResponseDto updatedLog = dailyLogService.addExerciseToLog(logId, requestDto);
        return ResponseEntity.ok(updatedLog);
    }

    /**
     * 특정 운동 기록을 수정하는 API
     */
    @PatchMapping("/exercises/{logExerciseId}")
    public ResponseEntity<Void> updateLogExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long logExerciseId,
            @RequestBody DailyLogExerciseUpdateRequestDto requestDto) {

        dailyLogService.updateLogExercise(userDetails.getUser().getUserId(), logExerciseId, requestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 운동 기록을 삭제하는 API
     */
    @DeleteMapping("/exercises/{logExerciseId}")
    public ResponseEntity<Void> deleteLogExercise(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long logExerciseId) {

        dailyLogService.deleteLogExercise(userDetails.getUser().getUserId(), logExerciseId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 현재 로그인한 사용자의 특정 날짜 일지를 조회하는 API
     * @param date 조회할 날짜 (yyyy-MM-dd 형식)
     * @return DailyLogResponseDto 리스트 반환
     */
    @GetMapping("/by-date")
    public ResponseEntity<DailyLogResponseDto> getLogsByDate(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("date") LocalDate date
    ) {
        Long userId = userDetails.getUser().getUserId();
        return ResponseEntity.ok(dailyLogService.getLogByDate(userId, date));
    }

    /**
     * 특정 일지에 AI 피드백을 저장(업데이트)하는 API
     * @param logId 피드백을 저장할 일지의 ID
     * @param feedback AI가 생성한 JSON 객체
     */
    @PutMapping("/{logId}/feedback")
    public ResponseEntity<Void> saveAiFeedback(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long logId,
            @RequestBody AIFeedback feedback
    ) {
        Long userId = userDetails.getUser().getUserId();
        dailyLogService.saveAiFeedback(userId, logId, feedback);

        return ResponseEntity.ok().build();
    }

    /**
     * 특정 일지에 저장된 AI 피드백을 조회하는 API
     * @param logId 피드백을 조회할 일지의 ID
     * @param userDetails 토큰에서 추출된 현재 로그인한 사용자 정보
     * @return AIFeedback JSON 객체
     */
    @GetMapping("/{logId}/feedback")
    public ResponseEntity<AIFeedback> getAiFeedback(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long logId
    ) {
        Long userId = userDetails.getUser().getUserId();
        AIFeedback feedback  = dailyLogService.getAiFeedback(userId, logId);
        return ResponseEntity.ok(feedback);
    }
}
