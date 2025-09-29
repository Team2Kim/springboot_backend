package com.example.nationalfitnessapp.dto;

import com.example.nationalfitnessapp.domain.Bookmark;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BookmarkResponseDto {

    private final Long bookmarkId;
    private final LocalDateTime createdAt;
    private final ExerciseInBookmarkDto exercise;

    public BookmarkResponseDto(Bookmark bookmark){
        this.bookmarkId = bookmark.getBookmarkId();
        this.createdAt = bookmark.getCreatedAt();
        this.exercise = new ExerciseInBookmarkDto(bookmark.getExercise());
    }
}
