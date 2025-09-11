package com.example.nationalfitnessapp.domain;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;;

import java.time.LocalDateTime;

@Entity
@Table(name = "Facility")
@Getter
@Setter
@NoArgsConstructor
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facilityId;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 30)
    private String groupName;

    @Column(nullable = false, length = 30)
    private String typeName;

    @Column(nullable = false, length = 30)
    private String categoryName;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(nullable = false, length = 100)
    private String postalCode;

    @Column(nullable = false, length = 100)
    private String roadAddress;

    @Column(nullable = false)  // Dto
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false, length = 30)
    private String phoneNumber;

    @Column(nullable = false)
    private boolean isOpen;

    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;

    public Facility(String name, String groupName, String typeName, String categoryName, String status, String postalCode,
                    String roadAddress, Double latitude, Double longitude, String phoneNumber, boolean isOpen, LocalDateTime lastUpdateDate){
        this.name = name;
        this.groupName = groupName;
        this.typeName = typeName;
        this.categoryName = categoryName;
        this.status = status;
        this.postalCode = postalCode;
        this.roadAddress = roadAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNumber = phoneNumber;
        this.isOpen = isOpen;
        this.lastUpdateDate = lastUpdateDate;
    }
}
