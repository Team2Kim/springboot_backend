package com.example.nationalfitnessapp.service;

import com.example.nationalfitnessapp.domain.Exercise;
import com.example.nationalfitnessapp.dto.ExerciseApiResponse;
import com.example.nationalfitnessapp.dto.ExerciseDto;
import com.example.nationalfitnessapp.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final WebClient webClient;

    @Value("${api.serviceKey}")
    private String serviceKey;

    // [수정] 테스트를 위해 guideUrl만 남김
    @Value("${exercise.api.baseUrl.guide}")
    private String guideUrl;

    /**
     * [수정] '운동 가이드' API의 첫 페이지만 가져와 저장하는 테스트용 메서드
     */
    @Transactional
    public void fetchAndSaveAllExercises() {
        log.info("운동 영상 데이터 저장 테스트를 시작합니다. ('운동 가이드' API 1페이지, 100개)");

        // 1. URL과 파라미터 설정 (1페이지, 100개로 고정)
        String url = UriComponentsBuilder.fromHttpUrl(guideUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", 1)
                .queryParam("numOfRows", 100)
                .queryParam("resultType", "xml")
                .build(true)
                .toUriString();

        // 2. API 호출
        ExerciseApiResponse apiResponse = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ExerciseApiResponse.class)
                .block();

        // 3. 데이터 추출
        if (apiResponse == null || apiResponse.getBody() == null || apiResponse.getBody().getItems() == null || apiResponse.getBody().getItems().getExerciseList() == null) {
            log.warn("API로부터 유효한 데이터를 받아오지 못했습니다.");
            return;
        }
        List<ExerciseDto> dtoList = apiResponse.getBody().getItems().getExerciseList();

        // 4. 데이터 저장
        int savedCount = 0;
        for (ExerciseDto dto : dtoList) {
            Exercise exercise = dto.toEntity();
            String fullUrl = exercise.getVideoUrl();

            if (fullUrl != null && !exerciseRepository.existsByVideoUrl(fullUrl)) {
                exerciseRepository.save(exercise);
                savedCount++;
            }
        }
        log.info("테스트 저장 완료. 총 {}개의 새로운 영상을 저장했습니다.", savedCount);
    }
}