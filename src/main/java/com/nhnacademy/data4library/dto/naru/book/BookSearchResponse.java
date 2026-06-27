package com.nhnacademy.data4library.dto.naru.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhnacademy.data4library.dto.naru.common.DocWrapped;
import com.nhnacademy.data4library.dto.naru.common.NaruBook;

import java.util.List;

public record BookSearchResponse(
        @JsonProperty("numFound")
        int numFound,

        @JsonProperty("docs")
        List<DocWrapped<NaruBook>> docs
) {}