package com.example.nationalfitnessapp.initializer;

import com.example.nationalfitnessapp.service.FacilityService;
import com.example.nationalfitnessapp.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component  // 1. 스프링이 이 클래스를 찾아 Bean으로 등록하게 함
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner{  // 2. ApplicationRunner 인터페이스 구현

    private final FacilityService facilityService;
    private final ExerciseService exerciseService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 4. 애플리케이션이 완전히 시작된 후, run 메서드가 자동으로 실행됨
        log.info("=============================================");
        log.info("========= 데이터 초기화 작업을 시작합니다. ========");

        // facilityService.fetchAndSaveFacilities();  // 서비스의 메서드 호출
        // exerciseService.fetchAndSaveAllExercises();  // 서비스의 메서드 호출

        log.info("======= 데이터 초기화 작업이 완료되었습니다. =======");
        log.info("=============================================");
    }
}
