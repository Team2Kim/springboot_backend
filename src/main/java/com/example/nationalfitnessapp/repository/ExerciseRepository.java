package com.example.nationalfitnessapp.repository;

import com.example.nationalfitnessapp.domain.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;  // 복잡한 동적 검색 조건을 실제로 실행시켜주는 실행기의 역할을 한다.

public interface ExerciseRepository extends JpaRepository<Exercise, Long>, JpaSpecificationExecutor<Exercise>{

    // videoUrl을 기준으로 데이터가 이미 존재하는지 확인하는 메서드
    boolean existsByVideoUrl(String videoUrl);
}
