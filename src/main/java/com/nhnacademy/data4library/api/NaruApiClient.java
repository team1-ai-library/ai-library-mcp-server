package com.nhnacademy.data4library.api;

import com.nhnacademy.data4library.annotation.ApiClient;
import com.nhnacademy.data4library.dto.NaruApiResponse;
import com.nhnacademy.data4library.dto.book.*;
import com.nhnacademy.data4library.dto.library.BookExistResponse;
import com.nhnacademy.data4library.dto.library.LibrarySearchResponse;
import com.nhnacademy.data4library.properties.NaruApiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

@Slf4j
@ApiClient
@RequiredArgsConstructor
public class NaruApiClient {

    private final NaruApiProperties naruApiProperties;
    private final RestClient restClient;

    private static final String API_CALL_LOGGING = "[NaruApiClient] API 호출 - URL: {}";


    // 도서 제목으로 검색
    public NaruApiResponse<BookSearchResponse> searchBooks(String title) {

        String url = UriComponentsBuilder.fromUriString("/srchBooks")
                .queryParam("authKey", this.naruApiProperties.apiKey())
                .queryParam("title", title)
                .queryParam("format", "json") // XML 대신 JSON으로 받겠다는 것
                .build()
                .encode(StandardCharsets.UTF_8) // URL 인코딩
                .toUriString();

        log.info(API_CALL_LOGGING, url);

        // RestClient로 GET 요청 보내고, 응답 JSON을 역직렬화
        return this.restClient
                .get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<NaruApiResponse<BookSearchResponse>>() {
                }); // 잭슨이 제네릭 타입을 역직렬화할 때 타입 정보가 런타임에 날라가버려서 ParameterizedTypeReference 씀
    }

    // 한 지역의 도서관 검색
    public NaruApiResponse<LibrarySearchResponse> searchLibraries(String region) {

        String url = UriComponentsBuilder.fromUriString("/libSrch")
                .queryParam("authKey", this.naruApiProperties.apiKey())
                .queryParam("region", region)
                .queryParam("format", "json")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        log.info(API_CALL_LOGGING, url);

        return this.restClient
                .get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<NaruApiResponse<LibrarySearchResponse>>() {
                });
    }

    // 특정 도서관에 그 도서가 소장되어 있는지 확인
    public NaruApiResponse<BookExistResponse> checkBookExists(String libCode, String isbn) {

        String url = UriComponentsBuilder.fromUriString("/bookExist")
                .queryParam("authKey", this.naruApiProperties.apiKey())
                .queryParam("libCode", libCode)
                .queryParam("isbn13", isbn)
                .queryParam("format", "json")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        log.info(API_CALL_LOGGING, url);

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<NaruApiResponse<BookExistResponse>>() {
                });
    }

    // 책을 통해 해당 책을 가지고 있는 도서관을 검색
    public NaruApiResponse<LibrarySearchResponse> searchLibrariesByBooks(String isbn, String region) {

        String url = UriComponentsBuilder.fromUriString("/libSrchByBook")
                .queryParam("authKey", naruApiProperties.apiKey())
                .queryParam("isbn", isbn)
                .queryParam("region", region)
                .queryParam("format", "json")
                .encode(StandardCharsets.UTF_8)
                .toUriString();


        log.info(API_CALL_LOGGING, url);

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<NaruApiResponse<LibrarySearchResponse>>() {
                });
    }

    // 도서 상세 정보 (srchDtlList)
    public NaruApiResponse<BookDetailResponse> searchBookDetail(String isbn13) {

        String url = UriComponentsBuilder.fromUriString("/srchDtlList")
                .queryParam("authKey", this.naruApiProperties.apiKey())
                .queryParam("isbn13", isbn13)
                .queryParam("format", "json")
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        log.info(API_CALL_LOGGING, url);

        return this.restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<NaruApiResponse<BookDetailResponse>>() {
                });
    }

    // Date 파싱 필요할지도
    // 인기 대출 도서 (loanItemSrch)
    public NaruApiResponse<LoanItemResponse> loanItemsSearch(String startDate, String endDate, String region) {

        String url = UriComponentsBuilder.fromUriString("/loanItemSrch")
                .queryParam("authKey", this.naruApiProperties.apiKey())
                .queryParam("startDt", startDate)
                .queryParam("endDt", endDate)
                .queryParam("region", region)
                .queryParam("format", "json")
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        log.info(API_CALL_LOGGING, url);

        return this.restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<NaruApiResponse<LoanItemResponse>>() {
                });
    }

    // searchDate는 LocalDate.now() ?
    // 대출 급상승 도서 (hotTrend)
    public NaruApiResponse<HotTrendResponse> searchHotTrendBooks(String searchDate) {

        String url = UriComponentsBuilder.fromUriString("/hotTrend")
                .queryParam("authKey", this.naruApiProperties.apiKey())
                .queryParam("searchDt", searchDate)
                .queryParam("format", "json")
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        log.info(API_CALL_LOGGING, url);

        return this.restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<NaruApiResponse<HotTrendResponse>>() {
                });
    }

    // 추천 도서 - mania(마니아) 혹은 reader(다독자)
    public NaruApiResponse<RecommendResponse> searchRecommendBooks(String isbn13, String type) {

        String url = UriComponentsBuilder.fromUriString("/recommandList")
                .queryParam("authKey", this.naruApiProperties.apiKey())
                .queryParam("isbn13", isbn13)
                .queryParam("type", type)
                .queryParam("format", "json")
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        log.info(API_CALL_LOGGING, url);

        return this.restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<NaruApiResponse<RecommendResponse>>() {
                });
    }

}