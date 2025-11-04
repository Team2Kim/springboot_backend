package com.example.nationalfitnessapp.service;

import com.example.nationalfitnessapp.dto.OpenAIRequestDto;
import com.example.nationalfitnessapp.dto.OpenAIResponseDto;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Slf4j
@Service
public class OpenAIService {

    private final WebClient webClient;
    private final String apiKey;

    public OpenAIService(WebClient.Builder webClientBuilder, @Value("${openai.api.key}") String apiKey){
        this.webClient = webClientBuilder
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.apiKey = apiKey;
    }

    /**
     * OpenAI API를 호출하여 운동 추천 응답을 받습니다.
     * @param prompt AI에게 전달할 프롬프트
     * @return AI의 응답 텍스트
     */
    public String getAiRecommendation(String prompt) {
        OpenAIRequestDto requestDto = new OpenAIRequestDto("gpt-4o-mini", prompt);

        try {
            OpenAIResponseDto response = webClient.post()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .bodyValue(requestDto)
                    .retrieve()
                    .bodyToMono(OpenAIResponseDto.class)
                    .block(); // 동기 방식으로 응답을 기다림

            if (response != null && response.getFirstChoiceContent() != null) {
                return response.getFirstChoiceContent().trim();
            }
        } catch (Exception e) {
            log.error("OpenAI API 호출 중 에러 발생: {}", e.getMessage());
        }
        return null; // 실패 시 null 반환
    }
}
