package com.nhnacademy.data4library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaruApiResponse<T>(
        @JsonProperty("response")
        T response
) {
}