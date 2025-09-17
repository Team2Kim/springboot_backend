package com.example.nationalfitnessapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    public WebClient webClient() {
        // 메모리 버퍼 사이즈를 늘리기 위한 설정
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 한 번에 받을 수 있는 데이터의 크기를 10MB로 증설
                .build();

        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies) // 새로운 설정을 WebClient에 적용
                .build();
    }
}