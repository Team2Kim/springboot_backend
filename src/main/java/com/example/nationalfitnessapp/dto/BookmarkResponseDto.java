package com.example.nationalfitnessapp.dto;

import com.example.nationalfitnessapp.domain.Bookmark;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BookmarkResponseDto {

    private final Long bookmarkId;
    private final Long exerciseId;
    private final String title;
    private final String videoUrl;
    private final LocalDateTime createdAt;

    public BookmarkResponseDto(Bookmark bookmark){
        this.bookmarkId = bookmark.getBookmarkId();
        this.exerciseId = bookmark.getExercise().getExerciseId();
        this.title = bookmark.getExercise().getTitle();
        this.videoUrl = bookmark.getExercise().getVideoUrl();
        this.createdAt = bookmark.getCreatedAt();
    }
}
