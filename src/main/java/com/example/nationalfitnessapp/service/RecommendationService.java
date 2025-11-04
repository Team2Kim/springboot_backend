package com.example.nationalfitnessapp.service;

import com.example.nationalfitnessapp.domain.Exercise;
import com.example.nationalfitnessapp.domain.User;
import com.example.nationalfitnessapp.dto.ExerciseResponseDto; // import 추가
import com.example.nationalfitnessapp.repository.ExerciseRepository;
import com.example.nationalfitnessapp.repository.ExerciseSpecification;
import com.example.nationalfitnessapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList; // import 추가
import java.util.Collections;
import java.util.List;
import java.util.Map; // import 추가
import java.util.stream.Collectors; // import 추가

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final OpenAIService openAIService; // [추가] OpenAI 서비스 주입

    /**
     * [수정] 사용자 맞춤형 AI 운동 추천 로직 (RAG 방식)
     * @return 추천된 Exercise 객체를 DTO로 변환한 리스트
     */
    public List<ExerciseResponseDto> recommendExercise(Long userId, int count) {
        // 1. 사용자 정보 조회 (AI 프롬프트의 기반)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 2. [Retrieval] 1차 필터링: 사용자의 기본 선호도에 맞는 운동 후보군 조회
        Specification<Exercise> spec = ExerciseSpecification.Empty();

        if (user.getTargetGroup() != null && !user.getTargetGroup().isEmpty()) {
            spec = spec.and(ExerciseSpecification.filterByTargetGroup(user.getTargetGroup()));
        }
        if (user.getFitnessLevelName() != null && !user.getFitnessLevelName().isEmpty()) {
            spec = spec.and(ExerciseSpecification.equalFitnessLevelName(user.getFitnessLevelName()));
        }
        if (user.getFitnessFactorName() != null && !user.getFitnessFactorName().isEmpty()) {
            spec = spec.and(ExerciseSpecification.equalFitnessFactorName(user.getFitnessFactorName()));
        }

        // 3. 조건에 맞는 운동 목록을 DB에서 모두 조회 (N+1 방지 필요 - Repository 수정 권장)
        List<Exercise> candidates = exerciseRepository.findAll(spec);

        if (candidates.isEmpty()) {
            return Collections.emptyList(); // 추천할 운동이 없으면 빈 리스트 반환
        }

        // 4. [Augmented] AI에게 전달할 프롬프트 생성
        String prompt = createRecommendationPrompt(user, candidates, count);

        // 5. [Generation] AI 호출
        log.info("AI에게 운동 추천을 요청합니다...");
        String aiResponse = openAIService.getAiRecommendation(prompt); // "제목1,제목2,제목3"

        if (aiResponse == null) {
            log.warn("AI 응답이 없습니다. 후보군에서 랜덤으로 반환합니다.");
            return getRandomExercises(candidates, count); // AI 실패 시 랜덤 추천
        }

        log.info("AI 응답: {}", aiResponse);

        // 6. AI 응답(제목 목록)을 실제 Exercise 객체 목록으로 변환
        List<String> recommendedTitles = List.of(aiResponse.split(","));

        // 조회된 후보군(candidates) 내에서 AI가 추천한 제목의 운동들을 찾음
        Map<String, Exercise> candidateMap = candidates.stream()
                .collect(Collectors.toMap(Exercise::getTitle, e -> e, (a, b) -> a)); // 중복 타이틀 처리

        List<ExerciseResponseDto> finalRecommendations = new ArrayList<>();
        for (String title : recommendedTitles) {
            Exercise exercise = candidateMap.get(title.trim());
            if (exercise != null) {
                finalRecommendations.add(new ExerciseResponseDto(exercise));
            }
        }
        return finalRecommendations;
    }

    // AI에게 보낼 프롬프트 생성 (RAG의 핵심)
    private String createRecommendationPrompt(User user, List<Exercise> candidates, int count) {
        StringBuilder prompt = new StringBuilder();
        prompt.append(String.format("당신은 전문 피트니스 큐레이터입니다. " +
                        "현재 사용자의 프로필은 다음과 같습니다: [연령대: %s, 운동 수준: %s, 주 운동 목적: %s]. ",
                user.getTargetGroup(), user.getFitnessLevelName(), user.getFitnessFactorName()));

        prompt.append(String.format("사용자가 이 조건에 맞는 운동을 %d개 추천해달라고 요청했습니다. " +
                "아래 [후보 운동 목록] 중에서 사용자의 목적에 가장 잘 맞고, " +
                "운동 부위가 다양하게 조합되도록 %d개의 운동을 골라주세요. \n\n", count, count));

        prompt.append("[후보 운동 목록]\n");
        for (int i = 0; i < Math.min(candidates.size(), 30); i++) { // 토큰 제한을 위해 최대 30개만 제공
            Exercise e = candidates.get(i);
            String muscleNames = e.getMuscles().stream().map(m -> m.getName()).collect(Collectors.joining(", "));
            prompt.append(String.format("- 제목: %s (주요 부위: %s, 사용 도구: %s)\n", e.getTitle(), muscleNames, e.getExerciseTool()));
        }

        prompt.append("\n대답은 다른 말 없이, 쉼표(,)로 구분된 운동 제목으로만 해주세요. (예: 스쿼트, 벤치프레스, 런지)");
        return prompt.toString();
    }

    // AI 호출 실패 시 사용할 비상 플랜
    private List<ExerciseResponseDto> getRandomExercises(List<Exercise> candidates, int count) {
        Collections.shuffle(candidates);
        return candidates.stream()
                .limit(count)
                .map(ExerciseResponseDto::new)
                .collect(Collectors.toList());
    }
}