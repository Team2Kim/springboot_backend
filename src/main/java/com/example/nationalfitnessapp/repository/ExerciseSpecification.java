package com.example.nationalfitnessapp.repository;

import com.example.nationalfitnessapp.domain.Exercise;
import org.springframework.data.jpa.domain.Specification;

public class ExerciseSpecification {

    public static Specification<Exercise> Empty() {
        return (root, query, criteriaBuilder) -> null;
    }

    public static Specification<Exercise> likeTitle(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + keyword + "%");
    }

    public static Specification<Exercise> equalTargetGroup(String targetGroup) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("targetGroup"), targetGroup);
    }

    public static Specification<Exercise> equalFitnessFactorName(String fitnessFactorName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("fitnessFactorName"), fitnessFactorName);
    }

    public static Specification<Exercise> equalBodyPart(String bodyPart) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("bodyPart"), bodyPart);
    }

    public static Specification<Exercise> equalExerciseTool(String exerciseTool) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("exerciseTool"), exerciseTool);
    }
}
