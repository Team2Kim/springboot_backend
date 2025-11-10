package com.example.nationalfitnessapp.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet; // Set과 HashSet import 추가
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 512, unique = true)
    private String videoUrl;

    @Column(length = 1000)
    private String description;

    // --- 운동 분류 정보 ---
    @Column(length = 255)
    private String standardTitle;

    @Column(length = 100)
    private String targetGroup;

    @Column(length = 100)
    private String fitnessFactorName;

    @Column(length = 100)
    private String fitnessLevelName;

    @Column(length = 100)
    private String bodyPart;

    // [삭제] 기존 muscleName 필드는 삭제되었습니다.

    @Column(length = 100)
    private String exerciseTool;

    // [추가] Muscle 엔티티와의 다대다 관계 매핑
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "exercise_muscle", // 실제 DB의 연결 테이블 이름
            joinColumns = @JoinColumn(name = "exercise_id"), // 이 엔티티(Exercise)를 참조하는 외래 키
            inverseJoinColumns = @JoinColumn(name = "muscle_id") // 반대쪽 엔티티(Muscle)를 참조하는 외래 키
    )
    private Set<Muscle> muscles = new HashSet<>();


    // --- 영상 메타 정보 ---
    @Column
    private Integer videoLengthSeconds;

    @Column(length = 50)
    private String resolution;

    @Column
    private Double fpsCount;

    // --- 파일 정보 ---
    @Column(length = 255)
    private String imageFileName;

    @Column(length = 512)
    private String imageUrl;

    @Column
    private Long fileSize;

    // --- 프로그램 정보 ---
    @Column(length = 100)
    private String trainingAimName;

    @Column(length = 100)
    private String trainingPlaceName;

    @Column(length = 100)
    private String trainingSectionName;

    @Column(length = 100)
    private String trainingStepName;

    @Column(length = 100)
    private String trainingSequenceName;

    @Column(length = 50)
    private String trainingWeekName;

    @Column(length = 50)
    private String repetitionCountName;

    @Column(length = 50)
    private String setCountName;

    // --- 기타 정보 ---
    @Column(length = 50)
    private String operationName;

    @Column(length = 50)
    private String jobYmd;

    // --- isGookmin100 플래그 ---
    @Column(nullable = false)
    private boolean isGookmin100;
}