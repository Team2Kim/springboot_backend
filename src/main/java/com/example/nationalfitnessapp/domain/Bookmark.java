package com.example.nationalfitnessapp.domain;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "Bookmark")
@Getter
@Setter
@NoArgsConstructor
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exerciseId", nullable = false)
    private Exercise exercise;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Bookmark(User user, Exercise exercise){
        this.user = user;
        this.exercise = exercise;
    }

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
