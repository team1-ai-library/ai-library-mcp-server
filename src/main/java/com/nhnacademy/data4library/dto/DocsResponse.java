package com.nhnacademy.data4library.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DocsResponse(
        @JsonProperty("doc")
        Doc doc
) {}