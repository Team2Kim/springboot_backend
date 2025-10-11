package com.example.nationalfitnessapp.domain;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private LocalDate date;

    @Column(columnDefinition = "TEXT")  // Mapping to TEXT type
    private String memo;

    @OneToMany(mappedBy = "dailyLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyLogExercise> dailyLogExercises = new ArrayList<>();

    public DailyLog(User user, LocalDate date, String memo){
        this.user = user;
        this.date = date;
        this.memo = memo;
    }

    public void addDailyLogExercise(DailyLogExercise dailyLogExercise) {
        this.dailyLogExercises.add(dailyLogExercise);

        // 무한 루프에 빠지지 않도록 체크
        if (dailyLogExercise.getDailyLog() != this) {
            dailyLogExercise.setDailyLog(this);
        }
    }
}
