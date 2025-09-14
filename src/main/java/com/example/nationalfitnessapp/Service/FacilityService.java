package com.example.nationalfitnessapp.service;

import com.example.nationalfitnessapp.domain.Facility;
import com.example.nationalfitnessapp.repository.FacilityRepository;
import com.example.nationalfitnessapp.dto.ApiResponse;
import com.example.nationalfitnessapp.dto.FacilityDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Slf4j  // 로그 출력을 위한 Lombok 어노테이션
@Service  // 서비스 클래스임을 알리는 어노테이션
@Transactional(readOnly = true)  // 클래스 전체에 기본적으로 readOnly 작업만 이루어지도록 설정하는 어노테이션
@RequiredArgsConstructor  // final 필드에 대한 생성자를 자동으로 만들어주는 Lombok 어노테이션
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final WebClient webClient;

    @Value("${api.serviceKey}")
    private String serviceKey;

    @Value("${facility.api.baseUrl}")
    private String baseUrl;

    /*
    * 공공데이터 API를 호출하여 체육 시설 데이터를 DB에 저장하는 메서드
    * */
    @Transactional
    public void fetchAndSaveFacilities() {
        log.info("체육 시설 데이터 API 호출 및 저장을 시작합니다.");
        int savedCount = 0;


        for (int i = 1; i < 147; i++) {  // xml.body를 확인했을 때 totalCount가 146746으로 확인되어서 100을 나눈 뒤 반올림값 1468번 반복
            // 1. UriComponentsBuilder를 사용해 전체 URL과 파라미터를 먼저 조합
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("pageNo", i)
                    .queryParam("numOfRows", 1000)
                    .queryParam("resultType", "xml")
                    .queryParam("cp_nm", "")  // 필수 파라미터는 아니지만, 데이터를 제대로 입력받으려면 최소한 하나 이상의 검색 조건 파라미터가 있어야 한다.
                    .build(true)
                    .toUriString();

            // 2. 완성된 URL을 uri 메서드에 직접 전달
            ApiResponse apiResponse = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(ApiResponse.class)
                    .block();

            // 3. 응답 데이터에서 실제 시설 목록(List<FacilityDto>) 추출
            List<FacilityDto> dtoList = extractFacilityDtos(apiResponse);
            if (dtoList.isEmpty()) {
                log.warn("API로부터 유효한 시설 데이터를 받아오지 못했거나, 모든 데이터가 이미 존재합니다.");
                continue;
            }

            // 4. DTO 리스트를 순회하며 Entity로 변환 후 저장
            for (FacilityDto dto : dtoList) {
                if (dto.getRoadAddress() != null && !dto.getRoadAddress().isEmpty()) {
                    if (!facilityRepository.existsByRoadAddress(dto.getRoadAddress())) {
                        Facility facility = dto.toEntity();
                        facilityRepository.save(facility);
                        savedCount++;
                    }
                }
            }
        }
        log.info("총 {}개의 새로운 시설 데이터 저장을 완료했습니다.", savedCount);
    }



    // 중첩된 ApiResponse 객체에서 FacilityDto 리스트를 안전하게 추출하는 헬퍼 메서드
    private List<FacilityDto> extractFacilityDtos(ApiResponse apiResponse){
        if (apiResponse != null &&
            apiResponse.getBody() != null &&
            apiResponse.getBody().getItems() != null &&
            apiResponse.getBody().getItems().getFacilityList() != null) {
            return apiResponse.getBody().getItems().getFacilityList();
        }
        return Collections.emptyList();
    }
}
