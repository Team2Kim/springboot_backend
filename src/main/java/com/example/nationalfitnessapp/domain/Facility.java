package com.example.nationalfitnessapp.domain;
import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Facility")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA를 위한 기본 생성자
@AllArgsConstructor  // 모든 필드를 받는 생성자 (선택사항)
@Builder  // 빌더 패턴 자동 생성
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String groupName;

    @Column(length = 100)
    private String typeName;

    @Column(length = 100)
    private String categoryName;

    @Column(length = 100)
    private String status;

    @Column(length = 100)
    private String postalCode;

    @Column(length = 100)
    private String roadAddress;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(length = 100)
    private String phoneNumber;

    @Column
    private LocalDateTime lastUpdateDate;

    @Column
    private String isNation;
}
