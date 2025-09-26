package com.example.nationalfitnessapp.repository;

import com.example.nationalfitnessapp.domain.Facility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class FacilityRepositoryImpl implements FaciltiyRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Facility> searchWithDistance(Double latitude, Double longitude, String name, String categoryName, Pageable pageable) {

        // 1. 네이티브 SQL 쿼리 생성을 위한 StringBuilder
        StringBuilder sql = new StringBuilder("SELECT f.* FROM facility f WHERE 1=1");
        Map<String, Object> parameters = new HashMap<>();

        // 2. 조건이 있을 경우 WHERE 절 추가
        if (name != null && !name.isEmpty()) {
            sql.append(" AND f.name LIKE :name");
            parameters.put("name", "%" + name + "%");
        }
        if (categoryName != null && !categoryName.isEmpty()) {
            // 2-1. 콤마(,)를 기준으로 키워드를 분리해 List로 만듭니다.
            List<String> keywords = Arrays.asList(categoryName.split(","));

            // 2-2. (f.category_name LIKE ? OR f.category_name LIKE ? ...) 형태의 SQL 구문을 생성합니다.
            sql.append(" AND (");
            for (int i = 0; i < keywords.size(); i++) {
                if (i > 0) {
                    sql.append(" OR ");
                }
                String paramName = "categoryKeyword" + i;
                sql.append("f.category_name LIKE :").append(paramName);

                // 2-3. 파라미터 맵에 각 키워드를 추가합니다. (앞뒤 공백 제거 및 % 와일드카드 추가)
                parameters.put(paramName, "%" + keywords.get(i).trim() + "%");
            }
            sql.append(")");
        }

        // 3. 거리순 정렬이 필요한 경우 ORDER BY 절 추가
        if (latitude != null && longitude != null) {
            String pointWKT = String.format("POINT(%f %f)", latitude, longitude);
            sql.append(" ORDER BY ST_DISTANCE_SPHERE(f.location, ST_PointFromText(:point, 4326))");
            parameters.put("point", pointWKT);
        } else if (pageable.getSort().isSorted()) {
            // JPQL의 Sort.toString()은 네이티브 쿼리와 호환되지 않을 수 있어 직접 처리
            // 예시: 이름순 기본 정렬
            sql.append(" ORDER BY f.name ASC");
        }

        // 4. 네이티브 쿼리 생성 및 파라미터 바인딩
        // createNativeQuery의 두 번째 인자로 결과를 매핑할 엔티티 클래스를 지정
        Query query = entityManager.createNativeQuery(sql.toString(), Facility.class);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        // 5. 페이징 처리
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Facility> facilities = query.getResultList();

        // 6. 전체 카운트를 위한 쿼리 실행
        long total = countFacilities(name, categoryName);

        return new PageImpl<>(facilities, pageable, total);
    }

    private long countFacilities(String name, String categoryName) {
        StringBuilder countSql = new StringBuilder("SELECT count(*) FROM facility f WHERE 1=1");
        Map<String, Object> countParameters = new HashMap<>();

        if (name != null && !name.isEmpty()) {
            countSql.append(" AND f.name LIKE :name");
            countParameters.put("name", "%" + name + "%");
        }
        if (categoryName != null && !categoryName.isEmpty()) {
            // 1. 콤마(,)를 기준으로 키워드 분리
            List<String> keywords = Arrays.asList(categoryName.split(","));

            // 2. (f.category_name LIKE ? OR f.category_name LIKE ? ...) SQL 구문 생성
            countSql.append(" AND (");
            for (int i = 0; i < keywords.size(); i++) {
                if (i > 0) {
                    countSql.append(" OR ");
                }
                String paramName = "categoryKeyword" + i;
                countSql.append("f.category_name LIKE :").append(paramName);

                // 3. 파라미터 맵에 키워드 추가
                countParameters.put(paramName, "%" + keywords.get(i).trim() + "%");
            }
            countSql.append(")");
        }

        Query countQuery = entityManager.createNativeQuery(countSql.toString());
        for (Map.Entry<String, Object> entry : countParameters.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        // 네이티브 카운트 쿼리는 BigInteger 또는 Long으로 반환될 수 있음
        return ((Number) countQuery.getSingleResult()).longValue();
    }
}
