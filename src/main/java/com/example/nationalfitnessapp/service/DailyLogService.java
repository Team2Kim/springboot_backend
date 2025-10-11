package com.example.nationalfitnessapp.service;

import com.example.nationalfitnessapp.domain.DailyLog;
import com.example.nationalfitnessapp.domain.DailyLogExercise;
import com.example.nationalfitnessapp.domain.Exercise;
import com.example.nationalfitnessapp.domain.User;
import com.example.nationalfitnessapp.dto.DailyLogCreateRequestDto;
import com.example.nationalfitnessapp.dto.DailyLogExerciseRequestDto;
import com.example.nationalfitnessapp.dto.DailyLogExerciseUpdateRequestDto;
import com.example.nationalfitnessapp.dto.DailyLogResponseDto;
import com.example.nationalfitnessapp.repository.DailyLogRepository;
import com.example.nationalfitnessapp.repository.ExerciseRepository;
import com.example.nationalfitnessapp.repository.UserRepository;
import com.example.nationalfitnessapp.repository.DailyLogExerciseRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyLogService {

    private final DailyLogRepository dailyLogRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final DailyLogExerciseRepository dailyLogExerciseRepository;

    /**
     * 날짜와 메모만으로 빈 운동 일지를 생성하는 메서드
     * @param userId 운동을 추가할 사용자 ID
     * @param requestDto date와 memo의 정보를 담고 있는 DTO
     * @return 빈 일지 DTO
     * */
    @Transactional
    public DailyLogResponseDto createLog(Long userId, DailyLogCreateRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        dailyLogRepository.findByUserAndDate(user, requestDto.getDate())
                .ifPresent(log -> {throw new IllegalStateException("해당 날짜에 이미 작성된 일지가 있습니다.");});

        DailyLog dailyLog = new DailyLog(user, requestDto.getDate(), requestDto.getMemo());
        DailyLog saveDailyLog = dailyLogRepository.save(dailyLog);

        return new DailyLogResponseDto(saveDailyLog);
    }

    /**
     * 기존 일지에 운동 기록을 하나 추가하는 메서드
     * @param logId 운동을 추가할 일지의 ID
     * @param requestDto 추가할 운동 정보 DTO
     * @return 운동이 추가된 후의 일지 상세 정보 DTO
     * */
    @Transactional
    public DailyLogResponseDto addExerciseToLog(Long logId, DailyLogExerciseRequestDto requestDto){
        DailyLog dailyLog = dailyLogRepository.findById(logId).orElseThrow(() -> new EntityNotFoundException("일지를 찾을 수 없습니다."));
        Exercise exercise = exerciseRepository.findById(requestDto.getExerciseId()).orElseThrow(() -> new EntityNotFoundException("운동 정보를 찾을 수 없습니다."));
        DailyLogExercise dailyLogExercise = new DailyLogExercise(dailyLog, exercise, requestDto.getIntensity(), requestDto.getExerciseTime());

        // 양방향 연관관계 메서드를 사용해 일지에 운동 기록 추가
        dailyLog.addDailyLogExercise(dailyLogExercise);

        // 변경된 DailyLog를 명시적으로 저장하고, 저장된 객체를 받아옴
        DailyLog savedDailyLog = dailyLogRepository.save(dailyLog);

        return new DailyLogResponseDto(savedDailyLog);
    }

    /**
     * 특정 일지의 메모를 수정합니다.
     * @param userId 권한을 확인하기 위해 받는 userId
     * @param logId 수정하고 싶은 일지 ID
     * @param memo 수정할 메모 내용
     */
    @Transactional
    public void updateLogMemo(Long userId, Long logId, String memo) {
        DailyLog dailyLog = dailyLogRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("일지를 찾을 수 없습니다."));

        // 소유권 확인
        if (!dailyLog.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("해당 일지에 대한 수정 권한이 없습니다.");
        }

        dailyLog.setMemo(memo); // Dirty Checking으로 자동 업데이트
    }

    /**
     * 특정 일지를 삭제합니다.
     * @param userId 권한을 확인하기 위해 받는 userId
     * @param logId 삭제하고 싶은 일지 ID
     */
    @Transactional
    public void deleteLog(Long userId, Long logId) {
        DailyLog dailyLog = dailyLogRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("일지를 찾을 수 없습니다."));

        if (!dailyLog.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("해당 일지에 대한 삭제 권한이 없습니다.");
        }

        dailyLogRepository.delete(dailyLog); // Cascade 설정으로 자식들도 함께 삭제
    }

    /**
     * 특정 운동 기록을 수정합니다.
     * @param userId 권한을 확인하기 위해 받는 userId
     * @param logExerciseId 수정하고 싶은 운동기록 ID
     * @param requestDto 업데이트 내용의 requestDto
     */
    @Transactional
    public void updateLogExercise(Long userId, Long logExerciseId, DailyLogExerciseUpdateRequestDto requestDto) {
        DailyLogExercise logExercise = dailyLogExerciseRepository.findById(logExerciseId)
                .orElseThrow(() -> new EntityNotFoundException("운동 기록을 찾을 수 없습니다."));

        if (!logExercise.getDailyLog().getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("해당 기록에 대한 수정 권한이 없습니다.");
        }

        logExercise.setIntensity(requestDto.getIntensity());
        logExercise.setExerciseTime(requestDto.getExerciseTime());
    }

    /**
     * 특정 운동 기록을 삭제합니다.
     * @param userId 권한을 확인하기 위해 받는 userId
     * @param logExerciseId 삭제하고 싶은 운동기록 ID
     */
    @Transactional
    public void deleteLogExercise(Long userId, Long logExerciseId) {
        DailyLogExercise logExercise = dailyLogExerciseRepository.findById(logExerciseId)
                .orElseThrow(() -> new EntityNotFoundException("운동 기록을 찾을 수 없습니다."));

        if (!logExercise.getDailyLog().getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("해당 기록에 대한 삭제 권한이 없습니다.");
        }

        dailyLogExerciseRepository.delete(logExercise);
    }

    /**
     * 특정 사용자의 특정 날짜 일지 상세 조회
     * @param userId 일지를 확인할 사용자의 ID
     * @param date 확인할 일지의 날짜
     * @return 해당 일자의 운동 일지 DTO
     * */
    public DailyLogResponseDto getLogByDate(Long userId, LocalDate date) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        DailyLog dailyLog = dailyLogRepository.findByUserAndDate(user, date).orElseThrow(() -> new EntityNotFoundException("해당 날짜의 일지를 찾을 수 없습니다."));

        // Entity를 DTO로 변환하여 반환
        return new DailyLogResponseDto(dailyLog);
    }
}
