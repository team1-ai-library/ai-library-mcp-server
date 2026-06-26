package com.nhnacademy.data4library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BookExistBody(
        @JsonProperty("errCode")
        String errorCode,

        @JsonProperty("error")
        String error,

        @JsonProperty("result")
        BookExistResult result
) {
}