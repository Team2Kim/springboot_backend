package com.example.nationalfitnessapp.domain;
import jakarta.persistence.*;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name = "User")
@Getter  // generate field's get methods automatically
@Setter  // generate field's set methods automatically
@NoArgsConstructor  // No argument default constructor generate automatically
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

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public User(String loginId, String passwordHash, String nickname, String email){
        this.loginId = loginId;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
        this.email = email;
    }

    @PrePersist  // Call this method when Entity generate
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

