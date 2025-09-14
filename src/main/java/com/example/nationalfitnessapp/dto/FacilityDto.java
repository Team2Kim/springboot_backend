package com.example.nationalfitnessapp.dto;

import com.example.nationalfitnessapp.domain.Facility;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class FacilityDto {

    // name은 필수라고 가정
    @XmlElement(name = "faci_nm")
    private String name;

    // name을 제외한 모든 필드에 required = false를 추가하여 파싱 에러 방지
    @XmlElement(name = "faci_gb_nm", required = false)
    private String groupName;

    @XmlElement(name = "ftype_nm", required = false)
    private String typeName;

    @XmlElement(name = "fcob_nm", required = false)
    private String categoryName;

    @XmlElement(name = "faci_stat_nm", required = false)
    private String status;

    @XmlElement(name = "faci_zip", required = false)
    private String postalCode;

    @XmlElement(name = "faci_addr", required = false)
    private String roadAddress;

    @XmlElement(name = "faci_lat", required = false)
    private Double latitude;

    @XmlElement(name = "faci_lot", required = false)
    private Double longitude;

    @XmlElement(name = "faci_tel_no", required = false)
    private String phoneNumber;

    @XmlElement(name = "updt_dt", required = false)
    private String lastUpdate;

    @XmlElement(name = "nation_yn", required = false)
    private String isNation;

    public Facility toEntity() {
        // lastUpdate 필드가 null이거나 비어있을 경우를 대비
        LocalDateTime lastUpdateDate = null;
        if (this.lastUpdate != null && !this.lastUpdate.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            lastUpdateDate = LocalDate.parse(this.lastUpdate, formatter).atStartOfDay();
        }

        // Builder 패턴을 사용하여 Facility 객체 생성
        return Facility.builder()
                .name(this.name)
                .groupName(this.groupName)
                .typeName(this.typeName)
                .categoryName(this.categoryName)
                .status(this.status)
                .postalCode(this.postalCode)
                .roadAddress(this.roadAddress)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .phoneNumber(this.phoneNumber)
                .lastUpdateDate(lastUpdateDate)
                .isNation(isNation)
                .build();
    }
}