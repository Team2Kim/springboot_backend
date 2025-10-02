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

    public static Specification<Exercise> likeTargetGroup(String targetGroup) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), "%" + targetGroup + "%");
    }

    public static Specification<Exercise> likeDescription(String condition) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("description"), "%" + condition + "%");
    }

    public static Specification<Exercise> equalExerciseTool(String exerciseTool) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("exerciseTool"), exerciseTool);
    }
}
