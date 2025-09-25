package com.example.nationalfitnessapp.repository;

import com.example.nationalfitnessapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이미 사용중인 loginId인지 확인하는 메서드
    boolean existsByLoginId(String loginId);

    // 이미 등록에 사용되었던 email인지 확인하는 메서드
    boolean existsByEmail(String email);

    // 이미 사용중인 닉네임인지 확인하는 메서드
    boolean existsByNickname(String nickname);

    // loginId를 기준으로 DB에서 사용자 정보를 가져오는 기능 (해당하는 사용자가 없을 수도 있으므로 Optional로 감싸서 반환
    Optional<User> findByLoginId(String loginId);
}
