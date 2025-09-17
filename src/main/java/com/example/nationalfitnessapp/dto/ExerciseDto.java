package com.example.nationalfitnessapp.dto;

import com.example.nationalfitnessapp.domain.Exercise;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class ExerciseDto {

    // 7개 API의 모든 필드를 optional로 선언
    @XmlElement(name = "aggrp_nm", required = false)
    private String aggrpNm;
    @XmlElement(name = "chck_se_nm", required = false)
    private String chckSeNm;
    @XmlElement(name = "data_type", required = false)
    private String dataType;
    @XmlElement(name = "ecrg_cycl_nm", required = false)
    private String ecrgCyclNm;
    @XmlElement(name = "fbctn_yr", required = false)
    private String fbctnYr;
    @XmlElement(name = "file_nm", required = false)
    private String fileNm;
    @XmlElement(name = "file_sz", required = false)
    private Long fileSz;
    @XmlElement(name = "file_type_nm", required = false)
    private String fileTypeNm;
    @XmlElement(name = "file_url", required = false)
    private String fileUrl;
    @XmlElement(name = "fps_cnt", required = false)
    private Double fpsCnt;
    @XmlElement(name = "frme_no", required = false)
    private Integer frmeNo;
    @XmlElement(name = "ftns_fctr_nm", required = false)
    private String ftnsFctrNm;
    @XmlElement(name = "ftns_lvl_nm", required = false)
    private String ftnsLvlNm;
    @XmlElement(name = "img_file_nm", required = false)
    private String imgFileNm;
    @XmlElement(name = "img_file_sn", required = false)
    private Integer imgFileSn;
    @XmlElement(name = "img_file_url", required = false)
    private String imgFileUrl;
    @XmlElement(name = "job_ymd", required = false)
    private String jobYmd;
    @XmlElement(name = "lang", required = false)
    private String lang;
    @XmlElement(name = "msrmt_part_nm", required = false)
    private String msrmtPartNm;
    @XmlElement(name = "nope_nm", required = false)
    private String nopeNm;
    @XmlElement(name = "oper_nm", required = false)
    private String operNm;
    @XmlElement(name = "resolution", required = false)
    private String resolution;
    @XmlElement(name = "rptt_tcnt_nm", required = false)
    private String rpttTcntNm;
    @XmlElement(name = "set_cnt_nm", required = false)
    private String setCntNm;
    @XmlElement(name = "snap_tm", required = false)
    private Double snapTm;
    @XmlElement(name = "tool_nm", required = false)
    private String toolNm;
    @XmlElement(name = "trng_aim_nm", required = false)
    private String trngAimNm;
    @XmlElement(name = "trng_mscl_nm", required = false)
    private String trngMsclNm;
    @XmlElement(name = "trng_nm", required = false)
    private String trngNm;
    @XmlElement(name = "trng_part_nm", required = false)
    private String trngPartNm;
    @XmlElement(name = "trng_plc_nm", required = false)
    private String trngPlcNm;
    @XmlElement(name = "trng_se_nm", required = false)
    private String trngSeNm;
    @XmlElement(name = "trng_sqnc_nm", required = false)
    private String trngSqncNm;
    @XmlElement(name = "trng_step_nm", required = false)
    private String trngStepNm;
    @XmlElement(name = "trng_week_nm", required = false)
    private String trngWeekNm;
    @XmlElement(name = "vdo_desc", required = false)
    private String vdoDesc;
    @XmlElement(name = "vdo_len", required = false)
    private Integer vdoLen;
    @XmlElement(name = "vdo_ttl_nm", required = false)
    private String vdoTtlNm;

    /**
     * DTO를 Exercise 엔티티로 변환하는 메서드.
     * 존재하는 필드 값만 직접 매핑합니다.
     */
    public Exercise toEntity() {
        String fullVideoUrl = (this.fileUrl != null && this.fileNm != null) ? this.fileUrl + this.fileNm : null;

        // [수정] 헬퍼 메서드 대신, 존재하는 필드 값 중 우선순위를 정해 직접 사용
        String finalBodyPart = (this.trngPartNm != null && !this.trngPartNm.isEmpty()) ? this.trngPartNm : this.msrmtPartNm;

        return Exercise.builder()
                .title(this.vdoTtlNm)
                .videoUrl(fullVideoUrl)
                .description(this.vdoDesc)
                .trainingName(this.trngNm)
                .targetGroup(this.aggrpNm)
                .fitnessFactorName(this.ftnsFctrNm)
                .fitnessLevelName(this.ftnsLvlNm)
                .bodyPart(finalBodyPart) // 계산된 최종 부위 정보
                .muscleName(this.trngMsclNm)
                .exerciseTool(this.toolNm) // toolNm 필드 값을 직접 사용
                .videoLengthSeconds(this.vdoLen)
                .resolution(this.resolution)
                .fpsCount(this.fpsCnt)
                .imageFileName(this.imgFileNm)
                .imageUrl(this.imgFileUrl)
                .fileSize(this.fileSz)
                .trainingAimName(this.trngAimNm)
                .trainingPlaceName(this.trngPlcNm)
                .trainingSectionName(this.trngSeNm)
                .trainingStepName(this.trngStepNm)
                .trainingSequenceName(this.trngSqncNm)
                .trainingWeekName(this.trngWeekNm)
                .repetitionCountName(this.rpttTcntNm)
                .setCountName(this.setCntNm)
                .operationName(this.operNm)
                .isGookmin100(true)
                .build();
    }
}