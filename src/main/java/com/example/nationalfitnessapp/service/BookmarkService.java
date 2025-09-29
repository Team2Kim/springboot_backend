package com.example.nationalfitnessapp.service;

import com.example.nationalfitnessapp.domain.Bookmark;
import com.example.nationalfitnessapp.domain.Exercise;
import com.example.nationalfitnessapp.domain.User;
import com.example.nationalfitnessapp.repository.BookmarkRepository;
import com.example.nationalfitnessapp.repository.ExerciseRepository;
import com.example.nationalfitnessapp.repository.UserRepository;
import com.example.nationalfitnessapp.dto.BookmarkResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;

    /**
     * 즐겨찾기를 추가하거나 제거하는 토글 메서드
     * @param userId 사용자 ID
     * @param exerciseId 운동 영상 ID
     * @return 생성된 북마크 정보
     * */
    @Transactional
    public Boolean toggleBookmark(Long userId, Long exerciseId){
        // 1. 사용자 ID와 운동 영상 ID로 각각 사용자와 운동 영상 엔티티를 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow(() -> new IllegalArgumentException("해당 ID의 운동 영상을 찾을 수 없습니다"));

        // 2. 기존의 북마크가 있는지 조회
        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findByUserAndExercise(user, exercise);

        if (bookmarkOptional.isPresent()) {
            // 3. 북마크가 이미 존재하면 -> 삭제
            bookmarkRepository.delete(bookmarkOptional.get());
            return false;
        } else {
            // 4. 북마크가 없으면 -> 추가
            Bookmark newBookmark = Bookmark.builder()
                    .user(user)
                    .exercise(exercise)
                    .build();
            bookmarkRepository.save(newBookmark);
            return true;
        }

    }

    /**
     * 유저가 추가한 즐겨찾기 목록을 확인하는 메서드
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 페이징된 증겨찾기 목록 Dto
     * */
    public Page<BookmarkResponseDto> getMyBookmarks(Long userId, Pageable pageable){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다."));

        // 1. Repository에서 Page<Bookmark>를 받아온다.
        Page<Bookmark> bookmarksPage = bookmarkRepository.findAllByUserOrderByCreatedAtDesc(user, pageable);

        // 2. 페이지의 map기능을 사용해 Page<BookmarkResponseDto>로 변환한다.
        return bookmarksPage.map(BookmarkResponseDto::new);
    }
}
