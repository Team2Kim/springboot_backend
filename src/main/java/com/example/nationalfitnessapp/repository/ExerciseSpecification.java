package com.example.nationalfitnessapp.repository;

import com.example.nationalfitnessapp.domain.Exercise;
import com.example.nationalfitnessapp.domain.Muscle;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

public class ExerciseSpecification {

    public static Specification<Exercise> Empty() {
        return (root, query, criteriaBuilder) -> null;
    }

    public static Specification<Exercise> likeTitle(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + keyword + "%");
    }

    public static Specification<Exercise> likeTargetGroup(String targetGroup) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), "%" + targetGroup + "%");
    }

    public static Specification<Exercise> likeDescription(String condition) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), "%" + condition + "%");
    }

    public static Specification<Exercise> equalExerciseTool(String exerciseTool) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("exerciseTool"), exerciseTool);
    }

    // targetGroup 검색 조건
    public static Specification<Exercise> filterByTargetGroup(String userTargetGroup) {
        // '공통'을 포함해야 하는 그룹 목록
        List<String> commonInclusiveGroups = Arrays.asList("유소년", "청소년", "성인");


        return (root, query, criteriaBuilder) -> {
            if (commonInclusiveGroups.contains(userTargetGroup)) {
                // 사용자의 타겟 그룹이 유소년, 청소년, 성인 중 하나일 경우
                return criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("targetGroup"), userTargetGroup),
                        criteriaBuilder.equal(root.get("targetGroup"), "공통")
                );
            } else {
                // 그 외는 정확히 일치하는 값만 검색
                return criteriaBuilder.equal(root.get("targetGroup"), userTargetGroup);
            }
        };
    }

    // fitnessLevelName 검색 조건
    public static Specification<Exercise> equalFitnessLevelName(String fitnessLevelName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("fitnessLevelName"), fitnessLevelName);
    }

    // fitnessFactorName 검색 조건
    public static Specification<Exercise> equalFitnessFactorName(String fitnessFactorName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("fitnessFactorName"), fitnessFactorName);
    }

    /**
     * 특정 근육들을 포함하는 운동을 검색하는 조건을 생성
     * @param muscleNames 검색할 근육 이름 리스트
     * @return 근육 검색 조건
     * */
    public static Specification<Exercise> containMuscles(List<String> muscleNames){
        return (root, query, criteriaBuilder) -> {
            // 쿼리에 DISTINCT를 적용하여 중복된 EXERCISE가 반환되는 것을 방지
            query.distinct(true);
            // Exercise와 Muscle을 조인한다.
            Join<Exercise, Muscle> muscleJoin = root.join("muscles");
            // 조인된 Muscle의 'name'이 muscleNames 리스트에 포함되는지 확인한다.
            return muscleJoin.get("name").in(muscleNames);
        };
    }
}
