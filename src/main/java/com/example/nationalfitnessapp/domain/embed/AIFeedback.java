package com.example.nationalfitnessapp.domain.embed;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AIFeedback {
    private String workout_evaluation;
    private String target_muscles;
    private RecommendationDetails recommendations;
    private List<String> next_target_muscles;
    private String encouragement;
}
