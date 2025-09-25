package com.example.nationalfitnessapp.service;

import com.example.nationalfitnessapp.domain.Facility;
import com.example.nationalfitnessapp.dto.FacilityApiResponse;
import com.example.nationalfitnessapp.dto.FacilityDto;
import com.example.nationalfitnessapp.dto.FacilityResponseDto;
import com.example.nationalfitnessapp.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final WebClient webClient;

    @Value("${api.serviceKey}")
    private String serviceKey;

    @Value("${facility.api.baseUrl}")
    private String baseUrl;

    /**
     * 공공데이터 API를 호출하여 모든 체육 시설 데이터를 DB에 저장하는 메서드.
     * 페이징 기법을 사용하여 전체 데이터를 순회합니다.
     */
    @Transactional
    public void fetchAndSaveFacilities() {
        log.info("체육 시설 데이터 API 호출 및 저장을 시작합니다.");
        final int TOTAL_COUNT = 146746;
        final int ROWS_PER_PAGE = 1000;
        int totalPages = (TOTAL_COUNT + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE;
        int totalSavedCount = 0;

        log.info("총 데이터: {}, 페이지당 데이터: {}, 총 페이지: {}", TOTAL_COUNT, ROWS_PER_PAGE, totalPages);

        for (int page = 1; page <= totalPages; page++) {
            log.info("시설 데이터 페이지 {}/{} 처리 중...", page, totalPages);
            String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                    .queryParam("serviceKey", serviceKey)
                    .queryParam("pageNo", page)
                    .queryParam("numOfRows", ROWS_PER_PAGE)
                    .queryParam("resultType", "xml")
                    .build(true)
                    .toUriString();

            FacilityApiResponse facilityApiResponse = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(FacilityApiResponse.class)
                    .block();

            List<FacilityDto> dtoList = extractFacilityDtos(facilityApiResponse);
            if (dtoList.isEmpty()) {
                log.warn("페이지 {}에서 유효한 데이터를 받아오지 못했습니다.", page);
                continue;
            }

            int savedCountInPage = 0;
            for (FacilityDto dto : dtoList) {
                if (dto.getRoadAddress() != null && !dto.getRoadAddress().isEmpty()) {
                    if (!facilityRepository.existsByRoadAddress(dto.getRoadAddress())) {
                        Facility facility = dto.toEntity();
                        facilityRepository.save(facility);
                        savedCountInPage++;
                    }
                }
            }
            totalSavedCount += savedCountInPage;
        }
        log.info("총 {}개의 새로운 시설 데이터 저장을 완료했습니다.", totalSavedCount);
    }

    private List<FacilityDto> extractFacilityDtos(FacilityApiResponse facilityApiResponse){
        if (facilityApiResponse != null && facilityApiResponse.getBody() != null && facilityApiResponse.getBody().getItems() != null && facilityApiResponse.getBody().getItems().getFacilityList() != null) {
            return facilityApiResponse.getBody().getItems().getFacilityList();
        }
        return Collections.emptyList();
    }

    /**
     * 지도 뷰 기능: 특정 지점 반경 내의 모든 시설을 검색합니다.
     * @param longitude 중심점의 경도 (X 좌표)
     * @param latitude  중심점의 위도 (Y 좌표)
     * @param radius    검색할 반경 (미터 단위)
     * @return          거리 정보가 포함된 시설 DTO 리스트
     */
    public List<FacilityResponseDto> findFacilitiesWithinRadius(double longitude, double latitude, int radius) {
        String pointWKT = String.format("POINT(%s %s)", latitude, longitude);
        List<Facility> facilities = facilityRepository.findFacilitiesWithinRadius(pointWKT, radius);

        return facilities.stream()
                .map(facility -> {
                    Double distance = calculateDistance(longitude, latitude, facility.getLocation());
                    return new FacilityResponseDto(facility, distance);
                })
                .collect(Collectors.toList());
    }

    /**
     * 목록 뷰 기능: 특정 지점에서 가까운 순서대로 시설을 검색합니다 (페이징 적용).
     * @param longitude 기준점의 경도 (X 좌표)
     * @param latitude  기준점의 위도 (Y 좌표)
     * @param pageable  페이징 정보 (페이지 번호, 페이지 크기)
     * @return          거리 정보가 포함된 페이징된 시설 DTO
     */
    public Page<FacilityResponseDto> findFacilitiesSortedByDistance(double longitude, double latitude, Pageable pageable) {
        String pointWKT = String.format("POINT(%s %s)", latitude, longitude);

        Page<Facility> facilityPage = facilityRepository.findFacilitiesOrderByDistance(pointWKT, pageable);

        return facilityPage.map(facility -> {
            Double distance = calculateDistance(longitude, latitude, facility.getLocation());
            return new FacilityResponseDto(facility, distance);
        });
    }

    /**
     * 두 좌표 간의 거리를 미터 단위로 계산하는 헬퍼 메서드
     * @param lon1      시작점의 경도
     * @param lat1      시작점의 위도
     * @param destPoint 도착점의 Point 객체
     * @return          두 지점 사이의 거리 (미터 단위, 근사치)
     */
    private Double calculateDistance(double lon1, double lat1, Point destPoint) {
        if (destPoint == null) {
            return null;
        }
        // JTS 라이브러리는 좌표를 Degree 단위로 계산하므로, 미터 단위로 변환 (근사치)
        // 1 degree ≈ 111,195 meters
        double degreeDistance = Math.sqrt(Math.pow(lon1 - destPoint.getX(), 2) + Math.pow(lat1 - destPoint.getY(), 2));
        return degreeDistance * 111195;
    }

    /**
     * 다양한 조건으로 시설을 검색하고, 거리 정보를 포함하여 반환하는 메서드
     * @param latitude 기준점의 위도
     * @param longitude 기준점의 경도
     * @param name (선택) 검색할 시설 이름 키워드
     * @param categoryName (선택) 검색할 카테고리명
     * @param pageable 페이징 정보
     * @return 페이징된 시설 DTO 목록
     * */
    public Page<FacilityResponseDto> searchFacilities(Double latitude, Double longitude, String name, String categoryName, Pageable pageable) {

        // Custom 메서드를 직접 호출
        Page<Facility> facilityPage = facilityRepository.searchWithDistance(latitude, longitude, name, categoryName, pageable);

        // 결과 DTO 변환
        return facilityPage.map(facility -> {
            Double distance = null;
            if (latitude != null && longitude != null) {
                distance = calculateDistance(longitude, latitude, facility.getLocation());
            }
            return new FacilityResponseDto(facility, distance);
        });
    }
}