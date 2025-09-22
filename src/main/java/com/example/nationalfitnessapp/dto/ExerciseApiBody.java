package com.example.nationalfitnessapp.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class ExerciseApiBody {

    @XmlElement(name = "items")
    private ExerciseApiItems items;

    // 이 필드를 추가해야 totalCount를 동적으로 가져올 수 있다.
    @XmlElement(name = "totalCount")
    private int totalCount;
}
