package com.nhnacademy.data4library;

import com.nhnacademy.data4library.properties.NaruApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(NaruApiProperties.class)
public class Data4libraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(Data4libraryApplication.class, args);
    }

}