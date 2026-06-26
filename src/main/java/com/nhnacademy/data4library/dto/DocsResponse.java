package com.nhnacademy.data4library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DocsResponse(

        @JsonProperty("errorCode")
        String errorCode,

        @JsonProperty("error")
        String error,

        @JsonProperty("numFound")
        int numFound,

        @JsonProperty("doc")
        Doc doc
) {}