package com.example.nationalfitnessapp.service;

import com.example.nationalfitnessapp.domain.Exercise;
import com.example.nationalfitnessapp.repository.ExerciseSpecification;
import com.example.nationalfitnessapp.dto.ExerciseApiResponse;
import com.example.nationalfitnessapp.dto.ExerciseDto;
import com.example.nationalfitnessapp.repository.ExerciseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final WebClient webClient;

    @Value("${api.serviceKey}")
    private String serviceKey;

    // application.properties에 정의한 7개의 Base URL 주입
    @Value("${exercise.api.baseUrl.trainingGuide}")
    private String guideUrl;
    @Value("${exercise.api.baseUrl.fitnessTest}")
    private String fitnessTestUrl;
    @Value("${exercise.api.baseUrl.muscleTraining}")
    private String muscleTrainingUrl;
    @Value("${exercise.api.baseUrl.standardFitness}")
    private String standardFitnessUrl;
    @Value("${exercise.api.baseUrl.weeklyProgram}")
    private String weeklyProgramUrl;
    @Value("${exercise.api.baseUrl.viewAll}")
    private String viewAllUrl;
    @Value("${exercise.api.baseUrl.trainingVideo}")
    private String trainingVideoUrl;

    /**
     * 모든 운동 영상 API를 호출하여 DB에 저장하는 작업을 총괄하는 메서드
     */
//    @Transactional
//    public void fetchAndSaveAllExercises() {
//        log.info("모든 운동 영상 데이터 저장을 시작합니다.");
//
//        int totalSavedCount = 0;
//        // 각 API를 순차적으로 호출하여 데이터를 가져오고, 저장된 개수를 누적
//        totalSavedCount += fetchAndSaveFromApi(guideUrl, "운동 가이드");
//        totalSavedCount += fetchAndSaveFromApi(fitnessTestUrl, "체력 인증");
//        totalSavedCount += fetchAndSaveFromApi(muscleTrainingUrl, "근력 운동");
//        totalSavedCount += fetchAndSaveFromApi(standardFitnessUrl, "표준 운동");
//        totalSavedCount += fetchAndSaveFromApi(weeklyProgramUrl, "주간 프로그램");
//        totalSavedCount += fetchAndSaveFromApi(viewAllUrl, "전체 목록");
//        totalSavedCount += fetchAndSaveFromApi(trainingVideoUrl, "일반 운동 영상");
//
//        log.info("모든 운동 영상 데이터 저장을 완료했습니다. 총 저장된 데이터 수: {}", totalSavedCount);
//    }
//
//    /**
//     * 데이터 다운로드 완료
//     * 특정 API의 전체 데이터를 페이징하여 가져오는 공통 헬퍼 메서드
//     * @param baseUrl 호출할 API의 Base URL
//     * @param apiName 로그에 표시할 API 이름
//     * @return 새로 저장된 데이터의 수
//     */
//    private int fetchAndSaveFromApi(String baseUrl, String apiName) {
//        log.info(">>> [{}] API 데이터 저장을 시작합니다.", apiName);
//
//        // 1. 첫 페이지를 호출하여 totalCount 가져오기
//        ExerciseApiResponse firstResponse = callApi(baseUrl, 1, 10);
//        if (firstResponse == null || firstResponse.getBody() == null) {
//            log.warn("[{}] API로부터 유효한 첫 응답을 받지 못했습니다. 이 API는 건너뜁니다.", apiName);
//            return 0;
//        }
//
//        // 🚨 중요: ExerciseApiBody.java에 totalCount 필드가 있어야 합니다.
//        // final int TOTAL_COUNT = firstResponse.getBody().getTotalCount();
//        // 우선은 임시로 매우 큰 값으로 설정합니다.
//        final int TOTAL_COUNT = firstResponse.getBody().getTotalCount(); // 나중에 DTO에 totalCount 필드를 추가하고 위 코드로 교체하세요.
//
//        if (TOTAL_COUNT == 0) {
//            log.info("[{}] API에 데이터가 없습니다.", apiName);
//            return 0;
//        }
//
//        final int ROWS_PER_PAGE = 1000; // 한 번에 1000개씩 요청하여 효율 증대
//        int totalPages = (TOTAL_COUNT + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE;
//        int totalSavedCountInApi = 0;
//        log.info("[{}] 총 데이터 약: {}, 페이지당 데이터: {}, 총 페이지: {}", apiName, TOTAL_COUNT, ROWS_PER_PAGE, totalPages);
//
//        // 2. 전체 페이지 순회
//        for (int page = 1; page <= totalPages; page++) {
//            log.info("[{}] 페이지 {}/{} 처리 중...", apiName, page, totalPages);
//            ExerciseApiResponse apiResponse = callApi(baseUrl, page, ROWS_PER_PAGE);
//
//            if (apiResponse == null || apiResponse.getBody() == null || apiResponse.getBody().getItems() == null || apiResponse.getBody().getItems().getExerciseList() == null) {
//                log.warn("[{}] 페이지 {}에서 유효한 데이터를 받아오지 못했습니다. 다음 페이지로 넘어갑니다.", apiName, page);
//                continue;
//            }
//            List<ExerciseDto> dtoList = apiResponse.getBody().getItems().getExerciseList();
//
//            if (dtoList.isEmpty()) {
//                log.info("[{}] 페이지 {}에 더 이상 데이터가 없어 이 API의 처리를 중단합니다.", apiName, page);
//                break; // 데이터가 없으면 루프 중단
//            }
//
//            int savedCountInPage = 0;
//            for (ExerciseDto dto : dtoList) {
//                Exercise exercise = dto.toEntity();
//                String fullUrl = exercise.getVideoUrl();
//
//                if (fullUrl != null && !exerciseRepository.existsByVideoUrl(fullUrl)) {
//                    exerciseRepository.save(exercise);
//                    savedCountInPage++;
//                }
//            }
//            totalSavedCountInApi += savedCountInPage;
//        }
//        log.info("<<< [{}] API에서 총 {}개의 새로운 영상을 저장했습니다.", apiName, totalSavedCountInApi);
//        return totalSavedCountInApi;
//    }

    /*
     * WebClient를 사용해 실제 API를 호출하는 공통 메서드
     */
    private ExerciseApiResponse callApi(String baseUrl, int pageNo, int numOfRows) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("pageNo", pageNo)
                    .queryParam("numOfRows", numOfRows)
                    .queryParam("resultType", "xml")
                    .build(true)
                    .toUriString();

            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(ExerciseApiResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("API 호출 중 에러 발생: baseUrl={}, pageNo={}, error={}", baseUrl, pageNo, e.getMessage());
            return null;
        }
    }

    /**
     * ID로 운동 영상 1건을 조회하는 메서드
     * @param exerciseId 조회할 운동 영상 데이터의 ID
     * @return 찾아낸 Exercise 엔티티
     */
    public Exercise findByExerciseId(long exerciseId){
        return exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 운동영상을 찾을 수 없습니다: " + exerciseId));
    }

    /**
     * 모든 운동 영상을 페이징하여 조회하거나, 키워드로 검색하는 메서드
     * @param keyword 검색할 키워드 (없으면 전체 조회)
     * @param targetGroup 타켓 그룹 (조건 검색에 사용)
     * @param bodyPart 운동 신체 부위 (조건 검색에 사용)
     * @param fitnessFactorName 운동 체력 항목 (조건 검색에 사용)
     * @param exerciseTool 운동 도구 (조건 검색에 사용)
     * @param disease 질환 항목 (조건 검색에 사용)
     * @param pageable 페이징 및 정렬 정보
     * @return 페이징된 Exercise 데이터
     * */
    public Page<Exercise> findAll(String keyword, String targetGroup, String fitnessFactorName,
                                  String bodyPart, String exerciseTool, String disease, Pageable pageable){

        // 1. 기본 검색 조건 생성
        Specification<Exercise> spec = ExerciseSpecification.Empty();

        // 2. 각 파라미터가 존재하면, AND 조건으로 레고 블록을 조립
        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(ExerciseSpecification.likeTitle(keyword));
        }
        if (targetGroup != null && !targetGroup.isEmpty()) {
            // [수정] '공통' 로직이 포함된 filterByTargetGroup 사용
            spec = spec.and(ExerciseSpecification.filterByTargetGroup(targetGroup));
        }
        if (fitnessFactorName != null && !fitnessFactorName.isEmpty()) {
            // [수정] AI로 보강한 'fitnessFactorName' 컬럼을 직접 비교
            spec = spec.and(ExerciseSpecification.equalFitnessFactorName(fitnessFactorName));
        }

        // [추가] 누락되었던 bodyPart 검색 로직 추가
        if (bodyPart != null && !bodyPart.isEmpty()) {
            spec = spec.and(ExerciseSpecification.equalBodyPart(bodyPart));
        }

        if (exerciseTool != null && !exerciseTool.isEmpty()) {
            spec = spec.and(ExerciseSpecification.equalExerciseTool(exerciseTool));
        }

        // [추가] 누락되었던 disease 검색 로직 추가
        if (disease != null && !disease.isEmpty()) {
            // '재활' 같은 키워드는 description을 검색
            spec = spec.and(ExerciseSpecification.likeDescriptionForDisease(disease));
        }

        // [추가] /by-muscle API와의 통합을 위한 muscles 로직은 이전에 제외했으므로,
        //      이 메서드는 muscles 파라미터를 받지 않는 버전입니다.
        //      (만약 muscles도 이 API에서 처리하려면 파라미터에 List<String> muscles 추가 및 if문 추가 필요)

        // 최종 완성된 조건으로 Repository에 조회 요청
        return exerciseRepository.findAll(spec, pageable);
    }

    /**
     * 특정 근육 부위(들)를 타겟으로 하는 운동 영상을 검색하는 메서드
     * @param muscleNames 검색할 근육 이름 목록
     * @param pageable 페이징 정보
     * @return 페이징된 Exercise 데이터
     * */
    public Page<Exercise> findExercisesByMuscles(List<String> muscleNames, Pageable pageable) {
        Specification<Exercise> spec = ExerciseSpecification.containMuscles(muscleNames);

        return exerciseRepository.findAll(spec, pageable);
    }
}