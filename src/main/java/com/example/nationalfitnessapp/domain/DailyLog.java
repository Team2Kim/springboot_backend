package com.example.nationalfitnessapp.domain;
import jakarta.persistence.*;
import com.example.nationalfitnessapp.domain.embed.AIFeedback;

import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DailyLog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Builder.Default
    private List<DailyLogExercise> dailyLogExercises = new ArrayList<>();

    // [추가] AI 일지 정보를 저장할 필드 추가
    @JdbcTypeCode(SqlTypes.JSON)   // 이 객체를 JSON 타입으로 매핑
    @Column(columnDefinition = "json")  // DB에도 JSON 타입으로 컬럼 생성
    private AIFeedback aiFeedback;

    public void addDailyLogExercise(DailyLogExercise dailyLogExercise) {
        this.dailyLogExercises.add(dailyLogExercise);

        // 무한 루프에 빠지지 않도록 체크
        if (dailyLogExercise.getDailyLog() != this) {
            dailyLogExercise.setDailyLog(this);
        }
    }
}
