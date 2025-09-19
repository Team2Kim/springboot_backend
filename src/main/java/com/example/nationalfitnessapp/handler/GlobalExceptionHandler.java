package com.example.nationalfitnessapp.handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 모든 @RestController에서 발생하는 예외를 여기서 처리합니다.
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class) // EntityNotFoundException이 발생하면 이 메서드가 실행됩니다.
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException e) {

        // HTTP 상태 코드 404 (Not Found)와 함께
        // Service에서 던진 예외 메시지를 응답 본문에 담아 반환합니다.
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}