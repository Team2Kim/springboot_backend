package com.example.nationalfitnessapp.domain;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "DailyLog")
@Getter
@Setter
@NoArgsConstructor
public class DailyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    // FK
    @ManyToOne(fetch = FetchType.LAZY)  // Many DailyLog, One User
    @JoinColumn(name = "userId", nullable = false)  // Mapping Column
    private User user;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(columnDefinition = "TEXT")  // Mapping to TEXT type
    private String memo;

    public DailyLog(User user, String memo){
        this.user = user;
        this.memo = memo;
    }

    @PrePersist  // Call this method when Entity generate
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }
}
