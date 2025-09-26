package com.example.nationalfitnessapp.service;

import com.example.nationalfitnessapp.domain.User;
import com.example.nationalfitnessapp.dto.TokenDto;
import com.example.nationalfitnessapp.repository.UserRepository;
import com.example.nationalfitnessapp.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원 가입 메서드 (성공하면 서버에 올리고 실패하면 실패 메시지 전송)
     * @param loginId 사용자가 선택한 로그인 ID
     * @param password 사용자가 선택한 패스워드 ID
     * @param nickname 사용자가 선택한 닉네임
     * @param email 사용자가 선택한 닉네임
     * @return 성공 메시지 반환
     * */
    @Transactional
    public User signUp(String loginId, String password, String nickname, String email){
        // 1. 중복 확인
        if (userRepository.existsByLoginId(loginId)){
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        if (userRepository.existsByNickname(nickname)){
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
        if (userRepository.existsByEmail(email)){
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        // 2. 비밀번호 암호화
        String hashedPassword = passwordEncoder.encode(password);

        // 3. 사용자 생성 및 저장
        User newUser = User.builder()
                .loginId(loginId)
                .passwordHash(hashedPassword)
                .nickname(nickname)
                .email(email)
                .build();

        // 4. UserRepository를 이용해 DB에 새로운 유저 정보 저장하면서 User 객체 리턴
        return userRepository.save(newUser);
    }

    /**
     * 로그인 메서드 (성공하면 토큰을 부여하고 실패하면 실패 메시지 전송)
     * @param loginId 사용자가 입력한 로그인 ID
     * @param password 사용자가 입력한 패스워드
     * @return 토큰과 함께 성공 메시지 반환
     * */
    @Transactional
    public TokenDto login(String loginId, String password){
        // 1. 아이디로 사용자 조회
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalArgumentException("없는 아이디이거나 비밀번호가 틀렸습니다."));

        // 2. 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPasswordHash())){
            throw new IllegalArgumentException("없는 아이디이거나 비밀번호가 틀렸습니다.");
        }

        // 3. JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.createTokens(loginId);

        // 4. Refresh 토큰을 사용자 정보에 업데이트
        user.updateRefreshToken(tokenDto.getRefreshToken());
        userRepository.save(user);  // 변경된 사용자 정보를 DB에 저장

        return tokenDto;
    }

    /**
     * 토큰 재설정 메서드
     * @param accessToken 클라이언트가 재설정을 요구하며 보낸 Access 토큰
     * @param refreshToken 클라이언트가 재설정을 요구하며 보낸 Refresh 토큰
     * @return 재설정된 Refresh Token 과 Access Token 반환
     * */
    @Transactional
    public TokenDto refreshToken(String accessToken, String refreshToken) {

        // 1. Refresh Token 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token 입니다.");
        }

        // 2. Access Token에서 사용자 정보(loginId) 가져오기 (만료되었어도 정보 추출은 가능)
        String loginId = jwtTokenProvider.parseClaims(accessToken).getSubject();
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 3. DB에 저장된 Refresh Token과 클라이언트가 보낸 Refresh Token이 일치하는지 확인
        if (!refreshToken.equals(user.getRefreshToken())){
            // 일치하지 않으면, 토큰이 탈취되었을 가능성이 있으므로 로그아웃 처리
            // 보안을 위해 DB의 Refresh Token을 null로 만들어 해당 토큰을 더 이상 못쓰게 처리
            user.updateRefreshToken(null);
            throw new IllegalArgumentException("토큰 정보가 일치하지 않습니다.");
        }

        // 4. 새로운 토큰 생성
        TokenDto newTokenDto = jwtTokenProvider.createTokens(loginId);

        // 5. DB에 새로운 Refresh 토큰으로 업데이트
        user.updateRefreshToken(newTokenDto.getRefreshToken());

        return newTokenDto;
    }
}
