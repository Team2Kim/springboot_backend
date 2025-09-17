package com.example.nationalfitnessapp.repository;
import com.example.nationalfitnessapp.domain.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long>{

    // videoUrl을 기준으로 데이터가 이미 존재하는지 확인하는 메서드
    boolean existsByVideoUrl(String videoUrl);
}
