package com.example.nationalfitnessapp.controller;

import com.example.nationalfitnessapp.domain.Exercise;
import com.example.nationalfitnessapp.security.UserDetailsImpl;
import com.example.nationalfitnessapp.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * 사용자의 프로필과 요청된 조건을 기반으로 운동 영상을 추천한다.
     * @param userDetails 토큰에서 추출된 현재 로그인한 사용자 정보
     * @param count 추천받을 영상 개수 기본값 3개
     * @return 추천된 Exercise 객체 리스트
     */
    @GetMapping
    public ResponseEntity<List<Exercise>> getRecommendations(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "3") int count
    ) {
        Long userId = userDetails.getUser().getUserId();
        List<Exercise> recommendations = recommendationService.recommendExercise(userId, count);
        return ResponseEntity.ok(recommendations);
    }

}
