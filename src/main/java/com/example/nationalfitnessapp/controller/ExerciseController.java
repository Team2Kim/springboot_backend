package com.example.nationalfitnessapp.controller;

import com.example.nationalfitnessapp.domain.Exercise;
import com.example.nationalfitnessapp.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // 이 클래스가 Rest API의 컨트롤러임을 나타내는 어노테이션
@RequestMapping("/api/exercises")  // 기본 경로를 '/api/exercise' 으로 설정
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    /*
    * 특정 ID의 운동 영상 정보를 조회하는 API
    * @param exerciseId URL 경로에서 받아온 ID 값
    * @return Exercise 객체 (JSON 형태로 자동 변환되어 응답)
    * */
    @GetMapping("/{exerciseId}")  // Get 요청, /api/exercises/{숫자} 형태로 URL에 매핑
    public Exercise getExerciseById(@PathVariable long exerciseId) {
        // URL 경로의 exerciseId값을 활용해 Exercise 엔티티를 찾아 반환
        return exerciseService.findByExerciseId(exerciseId);
    }
}
