package com.nhnacademy.data4library.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.data4library.annotation.ApiClient;
import com.nhnacademy.data4library.dto.NaruApiResponse;
import com.nhnacademy.data4library.dto.book.*;
import com.nhnacademy.data4library.dto.error.NaruErrorResponse;
import com.nhnacademy.data4library.dto.library.BookExistResponse;
import com.nhnacademy.data4library.dto.library.LibrarySearchResponse;
import com.nhnacademy.data4library.exception.NaruApiException;
import com.nhnacademy.data4library.properties.NaruApiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

@Slf4j
@ApiClient
@RequiredArgsConstructor
public class NaruApiClient {

    private final NaruApiProperties naruApiProperties;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    private <T> T execute(String url, TypeReference<NaruApiResponse<T>> typeRef) {

        log.info("[NaruApiClient] API 호출 - URL: {}", url);

        try {
            String raw = this.restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);

            // 에러 체크
            NaruApiResponse<NaruErrorResponse> errorCheck = this.objectMapper.readValue(
                    raw,
                    new TypeReference<>() {
                    }
            );

            if (errorCheck.response().hasError()) {
                throw new NaruApiException(
                        errorCheck.response().errorCode(),
                        errorCheck.response().error()
                );
            }
            // 실제 타입으로 역직렬화
            T result = this.objectMapper.readValue(raw, typeRef).response();

            log.info("[NaruApiClient] API 응답 결과: {}", result.toString());

            return result;
        } catch (NaruApiException e) {
            throw e;
        } catch (Exception e) {
            throw new NaruApiException("PARSE_ERROR", e.getMessage());
        }
    }

    // 도서 제목으로 검색
    public BookSearchResponse searchBooks(String title) {

        String url = UriComponentsBuilder.fromUriString("/srchBooks")
                .queryParam("authKey", this.naruApiProperties.apiKey())
                .queryParam("title", title)
                .queryParam("format", "json") // XML 대신 JSON으로 받겠다는 것
                .build()
                .encode(StandardCharsets.UTF_8) // URL 인코딩
                .toUriString();

        return this.execute(url, new TypeReference<>() {
        });
    }

    // 한 지역의 도서관 검색
    public LibrarySearchResponse searchLibraries(String region) {

        String url = UriComponentsBuilder.fromUriString("/libSrch")
                .queryParam("authKey", this.naruApiProperties.apiKey())
                .queryParam("region", region)
                .queryParam("format", "json")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        return this.execute(url, new TypeReference<>() {
        });
    }

    // 특정 도서관에 그 도서가 소장되어 있는지 확인
    public BookExistResponse checkBookExists(String libCode, String isbn) {

        String url = UriComponentsBuilder.fromUriString("/bookExist")
                .queryParam("authKey", this.naruApiProperties.apiKey())
                .queryParam("libCode", libCode)
                .queryParam("isbn13", isbn)
                .queryParam("format", "json")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        return this.execute(url, new TypeReference<>() {
        });
    }

    // 책을 통해 해당 책을 가지고 있는 도서관을 검색
    public LibrarySearchResponse searchLibrariesByBooks(String isbn, String region) {

        String url = UriComponentsBuilder.fromUriString("/libSrchByBook")
                .queryParam("authKey", naruApiProperties.apiKey())
                .queryParam("isbn", isbn)
                .queryParam("region", region)
                .queryParam("format", "json")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        return this.execute(url, new TypeReference<>() {
        });
    }

    // 도서 상세 정보 (srchDtlList)
    public BookDetailResponse searchBookDetail(String isbn13) {

        String url = UriComponentsBuilder.fromUriString("/srchDtlList")
                .queryParam("authKey", this.naruApiProperties.apiKey())
                .queryParam("isbn13", isbn13)
                .queryParam("format", "json")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        return this.execute(url, new TypeReference<>() {
        });
    }

    // Date 파싱 필요할지도
    // 인기 대출 도서 (loanItemSrch)
    // API는 /loanItemSrch지만, 어쩔 수 없다.
    public LoanItemResponse searchHotBooksByDateAndRegion(String startDate, String endDate, String region) {

        String url = UriComponentsBuilder.fromUriString("/loanItemSrch")
                .queryParam("authKey", this.naruApiProperties.apiKey())
                .queryParam("startDt", startDate)
                .queryParam("endDt", endDate)
                .queryParam("region", region)
                .queryParam("format", "json")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        return this.execute(url, new TypeReference<>() {
        });
    }

    // searchDate는 LocalDate.now() ?
    // 대출 급상승 도서 (hotTrend)
    public HotTrendResponse searchHotTrendBooks(String searchDate) {

        String url = UriComponentsBuilder.fromUriString("/hotTrend")
                .queryParam("authKey", this.naruApiProperties.apiKey())
                .queryParam("searchDt", searchDate)
                .queryParam("format", "json")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        return this.execute(url, new TypeReference<>() {
        });

    }

    // 추천 도서 - mania(마니아) 혹은 reader(다독자)
    public RecommendResponse searchRecommendBooks(String isbn13, String type) {

        String url = UriComponentsBuilder.fromUriString("/recommandList")
                .queryParam("authKey", this.naruApiProperties.apiKey())
                .queryParam("isbn13", isbn13)
                .queryParam("type", type)
                .queryParam("format", "json")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        return this.execute(url, new TypeReference<>() {
        });
    }
}