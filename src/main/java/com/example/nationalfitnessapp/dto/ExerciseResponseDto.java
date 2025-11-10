package com.example.nationalfitnessapp.dto;

import com.example.nationalfitnessapp.domain.Exercise;
import com.example.nationalfitnessapp.domain.Muscle;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ExerciseResponseDto {

    private final Long exerciseId;
    private final String title;
    private final String videoUrl;
    private final String description;
    private final String standardTitle;
    private final String targetGroup;
    private final String fitnessFactorName;
    private final String fitnessLevelName;
    private final String bodyPart;
    private final String exerciseTool;
    private final Integer videoLengthSeconds;
    private final String resolution;
    private final Double fpsCount;
    private final String imageFileName;
    private final String imageUrl;
    private final Long fileSize;
    private final String trainingAimName;
    private final String trainingPlaceName;
    private final String trainingSectionName;
    private final String trainingStepName;
    private final String trainingSequenceName;
    private final String trainingWeekName;
    private final String repetitionCountName;
    private final String setCountName;
    private final String operationName;
    private final String jobYmd;
    private final boolean isGookmin100;
    private final List<String> muscles;

    public ExerciseResponseDto(Exercise exercise) {
        this.exerciseId = exercise.getExerciseId();
        this.title = exercise.getTitle();
        this.videoUrl = exercise.getVideoUrl();
        this.description = exercise.getDescription();
        this.standardTitle = exercise.getStandardTitle();
        this.targetGroup = exercise.getTargetGroup();
        this.fitnessFactorName = exercise.getFitnessFactorName();
        this.fitnessLevelName = exercise.getFitnessLevelName();
        this.bodyPart = exercise.getBodyPart();
        this.exerciseTool = exercise.getExerciseTool();
        this.videoLengthSeconds = exercise.getVideoLengthSeconds();
        this.resolution = exercise.getResolution();
        this.fpsCount = exercise.getFpsCount();
        this.imageFileName = exercise.getImageFileName();
        this.imageUrl = exercise.getImageUrl();
        this.fileSize = exercise.getFileSize();
        this.trainingAimName = exercise.getTrainingAimName();
        this.trainingPlaceName = exercise.getTrainingPlaceName();
        this.trainingSectionName = exercise.getTrainingSectionName();
        this.trainingStepName = exercise.getTrainingStepName();
        this.trainingSequenceName = exercise.getTrainingSequenceName();
        this.trainingWeekName = exercise.getTrainingWeekName();
        this.repetitionCountName = exercise.getRepetitionCountName();
        this.setCountName = exercise.getSetCountName();
        this.operationName = exercise.getOperationName();
        this.jobYmd = exercise.getJobYmd();
        this.isGookmin100 = exercise.isGookmin100();
        this.muscles = exercise.getMuscles().stream()
                .map(Muscle::getName)
                .collect(Collectors.toList());
    }
}