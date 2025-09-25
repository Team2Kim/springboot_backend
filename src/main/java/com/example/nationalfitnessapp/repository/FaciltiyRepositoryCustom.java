package com.example.nationalfitnessapp.repository;

import com.example.nationalfitnessapp.domain.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FaciltiyRepositoryCustom {
    Page<Facility> searchWithDistance(Double latitude, Double longitude, String name, String categoryName, Pageable pageable);
}
