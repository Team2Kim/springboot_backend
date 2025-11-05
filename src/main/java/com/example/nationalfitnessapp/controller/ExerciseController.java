package com.example.nationalfitnessapp.controller;

import com.example.nationalfitnessapp.domain.Exercise;
import com.example.nationalfitnessapp.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@RestController  // 이 클래스가 Rest API의 컨트롤러임을 나타내는 어노테이션
@RequestMapping("/api/exercises")  // 기본 경로를 '/api/exercise' 으로 설정
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    /**
     * 운동 영상을 검색하고 페이징하여 반환하는 API
     * @param keyword (선택) 제목으로 검색할 키워드
     * @param page (선택) 요청할 페이지 번호 (0부터 시작)
     * @param size (선택) 한 페이지에 보여줄 데이터 개수 (기본 10개)
     * @param targetGroup (선택) 타켓 그룹 (조건 검색에 사용)
     * @param fitnessFactorName (선택) 운동 체력 항목 (조건 검색에 사용)
     * @param bodyPart (선택) 운동 신체 부위 (조건 검색에 사용)
     * @param exerciseTool (선택) 운동 도구 (조건 검색에 사용)
     * @param disease (선택) 질환 항목 (조건 검색에 사용)
     * @return 페이징된 Exercise 데이터
     * */
    @GetMapping
    public Page<Exercise> getAllExercise(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String targetGroup,
            @RequestParam(required = false) String fitnessFactorName,
            @RequestParam(required = false) String bodyPart,
            @RequestParam(required = false) String exerciseTool,
            @RequestParam(required = false) String disease,
            @RequestParam(required = false) String fitnessLevelName
    ) {
        // 1. 페이징 및 정렬 정보 생성 (시작 페이지, 한 번에 받을 크기, 정렬 필드)
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());

        // 2. 서비스를 호출하여 데이터 조회
        return exerciseService.findAll(keyword, targetGroup, fitnessFactorName, bodyPart, exerciseTool, disease, fitnessLevelName, pageable);
    }

    /**
     * 근육 부위를 기준으로 운동 영상을 검색하고 페이징하여 반환하는 API
     * @param muscles 검색할 근육 이름 목록(쉼표로 구분 가능)
     * @param page 요청할 페이지 번호 (0부터 시작)
     * @param size 한 페이지에 보여줄 데이터 개수 (기본값 10)
     * @return 페이징된 Exercise 데이터
     * */
    @GetMapping("/by-muscle")
    public Page<Exercise> getExercisesByMuscles(
            @RequestParam List<String> muscles,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // 이름순으로 정렬
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return exerciseService.findExercisesByMuscles(muscles, pageable);
    }
}
