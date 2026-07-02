package com.nhnacademy.data4library.env;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
@Slf4j
class SpringBootEnvironmentVariableTest {

    @Autowired
    Environment environment;

    @Test
    @DisplayName(".env 파일 환경변수 테스트 (도서관 정보나루 API KEY)")
    void naruApiKeyEnvVarTest() {

        String result = this.environment.getProperty("naru-api-key");
        log.info("도서관 정보나루 API KEY: {}", result);

        Assertions.assertNotNull(result, "도서관 정보나루 API KEY가 null이면 안 됩니다.");
    }
}