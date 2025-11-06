package com.example.nationalfitnessapp.domain.embed;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecommendationDetails {
    private String next_workout;
    private String improvements;
    private String precautions;
}
