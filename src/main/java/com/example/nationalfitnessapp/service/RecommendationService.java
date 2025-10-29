package com.example.nationalfitnessapp.service;


import com.example.nationalfitnessapp.domain.Exercise;
import com.example.nationalfitnessapp.domain.User;
import com.example.nationalfitnessapp.repository.ExerciseRepository;
import com.example.nationalfitnessapp.repository.ExerciseSpecification;
import com.example.nationalfitnessapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    /**
     * 사용자 맞춤형 운동 추천 로직
     */
    public List<Exercise> recommendExercise(Long userId, int count) {
        // 1. 사용자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 2. 사용자의 선호도를 기반으로 Specification(검색 조건 조립)
        Specification<Exercise> spec = ExerciseSpecification.Empty();

        // 2-1. 사용자가 지정한 기본 프로필 조건(연령대, 수준, 목적)
        if (user.getTargetGroup() != null) {
            spec = spec.and(ExerciseSpecification.filterByTargetGroup(user.getTargetGroup()));
        }
        if (user.getFitnessLevelName() != null) {
            spec = spec.and(ExerciseSpecification.equalFitnessLevelName(user.getFitnessLevelName()));
        }
        if (user.getFitnessFactorName() != null) {
            spec = spec.and(ExerciseSpecification.equalFitnessFactorName(user.getFitnessFactorName()));
        }

        // 3. 조건에 맞는 운동 목록을 DB에서 모두 조회
        List<Exercise> candidates = exerciseRepository.findAll(spec);

        // 4. 결과가 요청한 개수보다 많으면 랜덤으로 섞어서 반환
        if (candidates.size() > count) {
            Collections.shuffle(candidates);
            return candidates.subList(0, count);
        } else {
            return candidates;
        }
    }

}
