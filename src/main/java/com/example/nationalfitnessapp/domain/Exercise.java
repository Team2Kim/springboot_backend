package com.example.nationalfitnessapp.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA를 위한 기본 생성자
@AllArgsConstructor // 모든 필드를 받는 생성자 (Builder가 사용)
@Builder // 빌더 패턴 자동 생성
@Entity
@Table(name = "Exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exerciseId;

    @Column(nullable = false, length = 255) // 제목은 길 수 있으므로 넉넉하게
    private String title; // vdo_ttl_nm

    @Column(length = 512, unique = true) // URL은 길고 고유해야 함
    private String videoUrl; // file_url + file_nm

    @Column(length = 1000) // 설명은 매우 길 수 있음
    private String description; // vdo_desc

    // --- 운동 분류 정보 ---
    @Column(length = 255)
    private String trainingName; // trng_nm (운동명, 카테고리로 활용 가능)

    @Column(length = 100)
    private String targetGroup; // aggrp_nm (대상 연령층)

    @Column(length = 100)
    private String fitnessFactorName; // ftns_fctr_nm (체력 요인)

    @Column(length = 100)
    private String fitnessLevelName; // ftns_lvl_nm (체력 수준)

    @Column(length = 100)
    private String bodyPart; // trng_part_nm 또는 msrmt_part_nm

    @Column(length = 100)
    private String exerciseTool; // tool_nm (운동 도구)

    // --- 영상 메타 정보 ---
    @Column
    private Integer videoLengthSeconds; // vdo_len (영상 길이)

    @Column(length = 50)
    private String resolution; // resolution (해상도)

    @Column
    private Double fpsCount; // fps_cnt (초당 프레임 수)

    // --- 파일 정보 ---
    @Column(length = 255)
    private String imageFileName; // img_file_nm

    @Column(length = 512)
    private String imageUrl; // img_file_url

    @Column
    private Long fileSize; // file_sz (파일 크기)

    // --- 프로그램 정보 (프로그램형 API에만 존재) ---
    @Column(length = 100)
    private String trainingAimName; // trng_aim_nm (운동 목적)

    @Column(length = 100)
    private String trainingPlaceName; // trng_plc_nm (운동 장소)

    @Column(length = 100)
    private String trainingSectionName; // trng_se_nm (운동 구분)

    @Column(length = 100)
    private String trainingStepName; // trng_step_nm (운동 단계)

    @Column(length = 100)
    private String trainingSequenceName; // trng_sqnc_nm (운동 순서)

    @Column(length = 50)
    private String trainingWeekName; // trng_week_nm (운동 주차)

    @Column(length = 50)
    private String repetitionCountName; // rptt_tcnt_nm (반복 횟수)

    @Column(length = 50)
    private String setCountName; // set_cnt_nm (세트 횟수)

    // --- 기타 정보 ---
    @Column(length = 50)
    private String operationName; // oper_nm (운영 명칭)

    @Column(length = 50)
    private String jobYmd;

    // --- isGookmin100 플래그 ---
    // API 데이터는 isGookmin100 = true, 직접 입력 데이터는 isGookmin100 = false 로 사용
    @Column(nullable = false)
    private boolean isGookmin100;
}