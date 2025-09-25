package com.example.nationalfitnessapp.service;

import com.example.nationalfitnessapp.repository.UserRepository;
import com.example.nationalfitnessapp.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원 가입 메서드 (성공하면 서버에 올리고 실패하면 실패 메시지 전송)
     * @param loginId 사용자가 선택한 로그인 ID
     * @param password 사용자가 선택한 패스워드 ID
     * @param nickname 사용자가 선택한 닉네임
     * @param email 사용자가 선택한 닉네임
     * @return 성공 메시지 반환
     * */

    /**
     * 로그인 메서드 (성공하면 토큰을 부여하고 실패하면 실패 메시지 전송)
     * @param loginId 사용자가 입력한 로그인 ID
     * @param password 사용자가 입력한 패스워드
     * @return 토큰과 함께 성공 메시지 반환
     * */

    /**
     * 토큰 재설정 메서드
     * @param token 클라이언트가 재설정을 요구하며 보낸 Refresh 토큰
     * @return 재설정된 Refresh Token 과 Access Token 반환
     * */

}
