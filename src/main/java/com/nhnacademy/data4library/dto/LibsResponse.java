package com.nhnacademy.data4library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record LibsResponse(
        @JsonProperty("pageNo")
        String pageNo,

        @JsonProperty("pageSize")
        String pageSize,

        @JsonProperty("numFound")
        int numFound,

        @JsonProperty("resultNum")
        int resultNum,

        @JsonProperty("libs")
        List<LibWrapper> libs
) {
}