package com.example.nationalfitnessapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;  // 모든 필드를 파라미터로 가지는 생성자 자동 생성
import lombok.NoArgsConstructor;  // 파라미터가 없는 기본 생성자 자동 생성
import lombok.Getter; // 필드의 Get 메서드를 자동 생성한다.
import lombok.Setter;  // 필드의 Set 메서드를 자동 생성한다.
import lombok.Builder;  // 필요한 값만, 순서에 상관없이, 객체 생성이 가능하다. (예: User user = User.builder().loginId("newuser").passwordHash("hash"))

import java.time.LocalDateTime;
@Entity
@Table(name = "User")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto_Increment
    private Long userId;

    @Column(unique = true, nullable = false, length=20)
    private String loginId;

    @Column(nullable = false, length=255)
    private String passwordHash;

    @Column(unique = true, nullable = false, length=20)
    private String nickname;

    @Column(unique = true, nullable = false, length=100)
    private String email;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @Column(length = 512)
    private String refreshToken;

    @PrePersist  // 엔티티가 생성될 때 이 메서드를 호출한다.
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Refresh Token 업데이터를 위한 편의 메서드
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}