package com.nhnacademy.data4library.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "naru-api")
public record NaruApiProperties(
        String apiKey,
        String baseUrl
) {
}