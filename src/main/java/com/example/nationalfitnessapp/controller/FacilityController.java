package com.example.nationalfitnessapp.controller;

import com.example.nationalfitnessapp.service.FacilityService;
import com.example.nationalfitnessapp.dto.FacilityResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    /**
     * 지도에 표시할 시설 목록을 조회하는 API
     * @param lon 지도 중심의 경도
     * @param lat 지도 중심의 위도
     * @param radius 검색 반경 (meter), 기본값 3000m
     * @return 시설 목록
     * */
    @GetMapping("/map")
    public List<FacilityResponseDto> getFacilitiesForMap(
            @RequestParam double lon,
            @RequestParam double lat,
            @RequestParam(defaultValue = "3000") int radius
    ) {
        return facilityService.findFacilitiesWithinRadius(lon, lat, radius);
    }

    /**
     * 현재 위치에서 가까운 순서대로 시설 목록을 조회하는 API (페이징)
     * @param lon 현재 위치의 경도
     * @param lat 현재 위치의 위도
     * @param page 요청할 페이지 번호 (0부터 시작)
     * @param size 한 페이지에 보여줄 데이터 개수 (기본값 10)
     * @return 페이징된 시설 목록
     * */
    @GetMapping("/list")
    public Page<FacilityResponseDto> getFacilitiesForList(
            @RequestParam double lon,
            @RequestParam double lat,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return facilityService.findFacilitiesSortedByDistance(lon, lat, pageable);
    }

    /**
     * 시설을 이름 또는 카테고리로 검색하는 API
     * @param lat 현재 위치의 위도
     * @param lon 현재 위치의 경도
     * @param name (선택) 검색할 시설 이름 키워드
     * @param categoryName (선택) 조건 검색에 활용할 카테고리명
     * @param page (선택) 요청할 페이지 번호 (0부터 시작)
     * @param size 한 페이지에 보여줄 데이터 개수 (기본값 10)
     * @return 페이징된 시설 목록
     * */
    @GetMapping("/search")
    public Page<FacilityResponseDto> searchFacilities(
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return facilityService.searchFacilities(lat, lon, name, categoryName, pageable);
    }
}
