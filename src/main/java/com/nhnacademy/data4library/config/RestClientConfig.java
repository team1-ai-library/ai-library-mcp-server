package com.nhnacademy.data4library.config;

import com.nhnacademy.data4library.properties.NaruApiProperties;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final NaruApiProperties naruApiProperties;

    @Bean
    public RestClient restClient() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(naruApiProperties.baseUrl());
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        return RestClient.builder()
                .baseUrl(this.naruApiProperties.baseUrl())
//                .uriBuilderFactory(factory)
                .build();
    }
}