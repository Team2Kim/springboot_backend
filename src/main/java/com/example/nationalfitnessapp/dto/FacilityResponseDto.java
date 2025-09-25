package com.example.nationalfitnessapp.dto;

import com.example.nationalfitnessapp.domain.Facility;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FacilityResponseDto {

    private final Long facilityId;
    private final String name;
    private final String groupName;
    private final String typeName;
    private final String categoryName;
    private final String status;
    private final String postalCode;
    private final String roadAddress;
    private final Double latitude;
    private final Double longitude;
    private final String phoneNumber;
    private final LocalDateTime lastUpdateDate;
    private final String isNation;
    private final Double distance;

    public FacilityResponseDto(Facility facility, Double distance) {
        this.facilityId = facility.getFacilityId();
        this.name = facility.getName();
        this.groupName = facility.getGroupName();
        this.typeName = facility.getTypeName();
        this.categoryName = facility.getCategoryName();
        this.status = facility.getStatus();
        this.postalCode = facility.getPostalCode();
        this.roadAddress = facility.getRoadAddress();
        this.phoneNumber = facility.getPhoneNumber();
        this.lastUpdateDate = facility.getLastUpdateDate();
        this.isNation = facility.getIsNation();

        // Entity의 Point 객체에서 위도(Y), 경도(X) 값을 추출하여 DTO 필드에 할당합니다.
        // 이 과정을 통해 JSON 변환 시 무한 루프 에러를 방지합니다.
        if (facility.getLocation() != null) {
            this.latitude = facility.getLocation().getY();
            this.longitude = facility.getLocation().getX();
        } else {
            this.latitude = null;
            this.longitude = null;
        }
        this.distance = distance;
    }
}