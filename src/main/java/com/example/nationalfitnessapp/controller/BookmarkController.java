package com.example.nationalfitnessapp.controller;

import com.example.nationalfitnessapp.dto.BookmarkResponseDto;
import com.example.nationalfitnessapp.service.BookmarkService;
import com.example.nationalfitnessapp.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;


    /**
     * 즐겨찾기 상태를 토글하는 API
     * @param exerciseId 즐겨찾기할 운동 영상의 ID
     * @param userDetails 토큰에서 추출된 현재 로그인한 사용자 정보
     * @return 생성 시 201 Created, 삭제 시 204 No Content
     * */
    @PostMapping("/{exerciseId}")
    public ResponseEntity<Void> toggleBookmark(
            @PathVariable Long exerciseId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        // 토큰에 담겨있던 사용자 ID를 안전하게 사용
        Long currentUserId = userDetails.getUser().getUserId();

        boolean isBookmarked = bookmarkService.toggleBookmark(currentUserId, exerciseId);

        if (isBookmarked) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    /**
     * 사용자의 즐겨찾기 목록을 페이징하여 조회하는 API
     * @param page 요청할 페이지 번호 (0부터 시작, 기본값 0)
     * @param size 한 페이지에 보여줄 데이터 개수 (기본값 10)
     * @return 페이징된 즐겨찾기 목록
     * */
    @GetMapping
    public ResponseEntity<Page<BookmarkResponseDto>> getMyBookmarks(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // 토큰에 담겨있던 사용자 ID를 안전하게 사용
        Long currentUserId = userDetails.getUser().getUserId();

        Pageable pageable = PageRequest.of(page, size);
        Page<BookmarkResponseDto> bookmarks = bookmarkService.getMyBookmarks(currentUserId, pageable);
        return ResponseEntity.ok(bookmarks);
    }
}
