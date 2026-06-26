package com.nhnacademy.data4library.agent;

import com.nhnacademy.data4library.annotation.Agent;
import com.nhnacademy.data4library.api.NaruApiClient;
import lombok.RequiredArgsConstructor;

@Agent
@RequiredArgsConstructor
public class SearchBooksAgent {

    private final SearchBookDetailAgent searchBookDetailAgent;
    private final SearchHotBooksAgent searchHotBooksAgent;
    private final SearchRecommendBooksAgent searchRecommendBooksAgent;
    private final CheckBookExistsAgent checkBookExistAgent;
    private final NaruApiClient naruApiClient;
}