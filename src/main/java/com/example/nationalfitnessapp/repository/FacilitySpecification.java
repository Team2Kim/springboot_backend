package com.example.nationalfitnessapp.repository;

import com.example.nationalfitnessapp.domain.Facility;
import org.springframework.data.jpa.domain.Specification;
import java.util.Arrays;
import java.util.List;

public class FacilitySpecification {

    public static Specification<Facility> Empty() { return (root, query, criteriaBuilder) -> null; }

    /**
     * 이름(name)에 특정 키워드가 포함되는 조건을 생성한다. (like 검색)
     * @param name 검색할 키워드
     * @return 이름 검색 조건(Specification)
     * */
    public static Specification<Facility> likeName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    /**
     * 카테고리(categoryName)로 검색하는 조건을 생성
     * "기타"를 검색하면 주요 6개 카테고리를 제외한 나머지를 검색한다.
     * @param categoryName 검색할 카테고리명
     * @return 카테고리 검색 조건(Specification)
     * */
    public static Specification<Facility> filterByCategoryName(String categoryName){
        // 주요 6개 카테고리 목록
        List<String> mainCategories = Arrays.asList("축구장", "야구장", "체육관", "수영장", "풋살장", "종합체육시설");

        return (root, query, criteriaBuilder) -> {
            if ("기타".equals(categoryName)) {
                // '기타'인 경우: 주요 카테고리 목록에 포함되지 않는 (NOT INT) 데이터 검색
                return criteriaBuilder.not(root.get("categoryName").in(mainCategories));
            } else {
                return criteriaBuilder.equal(root.get("categoryName"), categoryName);
            }
        };
    }


}
