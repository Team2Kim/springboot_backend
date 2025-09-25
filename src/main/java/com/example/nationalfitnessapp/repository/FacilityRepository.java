package com.example.nationalfitnessapp.repository;

import com.example.nationalfitnessapp.domain.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

// JpaRepository를 상속받고, 제네릭으로 <관리할 엔티티, 엔티티 ID 타입>을 지정한다.
public interface FacilityRepository extends JpaRepository<Facility, Long>, JpaSpecificationExecutor<Facility>, FaciltiyRepositoryCustom{

    /*
    * 데이터 중복 방지 메서드
    * 목적: 데이터 무결성 및 신뢰도 유지, 불필요한 리소스 낭비 방지, 버그 예방
    * 작동 원리
    * 단순한 메서드 선언처럼 보이지만, Spring Data JPA가 메서드 이름을 분석해,
    * 자동으로 "SELECT COUNT(*) Facility WHERE road_address = ?" SQL 쿼리를 생성하고 실행한다. (결과가 false일때 저장)
    * Service 클래스에서 이 함수를 사용해 중복이 아닌 데이터만 통과시키는 방식으로 사용한다.
    * */
    boolean existsByRoadAddress(String roadAddress);

    // [수정] 반환 타입을 간단하게 Page<Facility>로 변경
    @Query(value = "SELECT * FROM facility f " +
            "ORDER BY ST_DISTANCE_SPHERE(f.location, ST_PointFromText(:point, 4326))",
            countQuery = "SELECT count(*) FROM facility",
            nativeQuery = true)
    Page<Facility> findFacilitiesOrderByDistance(@Param("point") String point, Pageable pageable);

    // [수정] 지도 API도 간단하게 List<Facility>를 반환하도록 변경
    @Query(value = "SELECT * FROM facility f " +
            "WHERE ST_DISTANCE_SPHERE(f.location, ST_PointFromText(:point, 4326)) <= :radius " +
            "ORDER BY ST_DISTANCE_SPHERE(f.location, ST_PointFromText(:point, 4326))",
            nativeQuery = true)
    List<Facility> findFacilitiesWithinRadius(@Param("point") String point, @Param("radius") int radius);

    //
}
