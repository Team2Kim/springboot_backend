package com.example.nationalfitnessapp.domain;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Exercise")
@Getter
@Setter
@NoArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 100)
    private String category;

    @Column(length = 100)
    private String exerciseTool;

    @Column(length = 100)
    private String bodyPart;

    @Column(length = 100)
    private String targetGroup;

    @Column(length = 255)
    private String videoUrl;

    @Column(nullable = false)
    private boolean isgookmin100;

    public Exercise(String title, String category, String exerciseTool, String bodyPart, String targetGroup, String videoUrl){
        this.title = title;
        this.category = category;
        this.exerciseTool = exerciseTool;
        this.bodyPart = bodyPart;
        this.targetGroup = targetGroup;
        this.videoUrl = videoUrl;
        this.isgookmin100 = true;
    }

    public Exercise(String title, String category, String exerciseTool, String bodyPart, String targetGroup){
        this.title = title;
        this.category = category;
        this.exerciseTool = exerciseTool;
        this.bodyPart = bodyPart;
        this.targetGroup = targetGroup;
        this.isgookmin100 = false;
    }
}
