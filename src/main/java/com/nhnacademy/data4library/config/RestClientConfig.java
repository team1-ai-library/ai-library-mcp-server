package com.nhnacademy.data4library.config;

import com.nhnacademy.data4library.properties.NaruApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final NaruApiProperties naruApiProperties;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(this.naruApiProperties.baseUrl())
                .build();
    }
}