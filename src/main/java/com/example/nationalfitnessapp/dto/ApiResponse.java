package com.example.nationalfitnessapp.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "response")  // 최상위 태그가 <response>임을 명시
@XmlAccessorType(XmlAccessType.FIELD)
public class ApiResponse {

    // <body> 태그에 대한 정보를 ApiBody 객체에 매핑
    @XmlElement(name = "body")
    private ApiBody body;
}
