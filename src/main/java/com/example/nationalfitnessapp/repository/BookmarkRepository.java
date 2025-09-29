package com.example.nationalfitnessapp.repository;

import com.example.nationalfitnessapp.domain.Bookmark;
import com.example.nationalfitnessapp.domain.User;
import com.example.nationalfitnessapp.domain.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>{

    // 특정 사용자가 특정 운동 영상을 이미 북마크했는지 확인
    Optional<Bookmark> findByUserAndExercise(User user, Exercise exercise);

    // 특정 사용자의 모든 북마크 목록을 최신순으로 조회
    @Query("SELECT b FROM Bookmark b JOIN FETCH b.exercise WHERE b.user = :user ORDER BY b.createdAt DESC")
    Page<Bookmark> findBookmarkBysWithExerciseByUser(@Param("user") User user, Pageable pageable);
}
