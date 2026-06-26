package com.nhnacademy.data4library.dto.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NaruErrorResponse(

        @JsonProperty("errCode")
        String errorCode,

        @JsonProperty("error")
        String error
) {
    public boolean hasError() {
        return Objects.nonNull(this.errorCode);
    }
}