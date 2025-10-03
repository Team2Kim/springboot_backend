package com.example.nationalfitnessapp.repository;

import com.example.nationalfitnessapp.domain.Muscle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MuscleRepository extends JpaRepository<Muscle, Long> {

    List<Muscle> findByNameIn(List<String> names);
}
