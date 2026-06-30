package com.nhnacademy.data4library.api;

import com.nhnacademy.data4library.dto.naru.book.*;
import com.nhnacademy.data4library.dto.naru.library.BookExistsResponse;
import com.nhnacademy.data4library.dto.naru.library.LibrarySearchResponse;
import com.nhnacademy.data4library.exception.NaruApiException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@Slf4j
@SpringBootTest
class NaruApiClientTest {

    @Autowired
    private NaruApiClient naruApiClient;

    @MockitoBean
    private RestClient restClient;

    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    private RestClient.ResponseSpec responseSpec;

    private static final String ERROR_JSON =
            """
                    {"response": {"errCode": "003", "error": "authKey가 유효하지 않습니다."}}
                    """;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        responseSpec = mock(RestClient.ResponseSpec.class);

        given(restClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.uri(anyString())).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.retrieve()).willReturn(responseSpec);
    }

    private void givenResponse(String json) {
        given(responseSpec.body(String.class)).willReturn(json);
    }

    // ===================== searchBooks =====================

    @Test
    @DisplayName("searchBooks - NaruApiResponse 정상 응답")
    void searchBooks_success() {
        String json = """
                {
                  "response": {
                    "numFound": 1,
                    "docs": [
                      {
                        "doc": {
                          "bookname": "소년이 운다",
                          "authors": "황석영 지음",
                          "publisher": "창비",
                          "publication_year": "2010",
                          "isbn13": "9788936433840",
                          "addition_symbol": "",
                          "vol": "",
                          "class_no": "813.6",
                          "class_nm": "한국소설",
                          "bookImageURL": "https://image.aladin.co.kr/product/14/69/cover200/8936433844_3.jpg",
                          "bookDtlUrl": "http://www.nl.go.kr/seoji/SearchDetail.do?isbn=9788936433840",
                          "loan_count": "100"
                        }
                      }
                    ]
                  }
                }
                """;
        givenResponse(json);

        BookSearchResponse response = naruApiClient.searchBooks("소년이 운다");

        log.info("[searchBooks 정상 응답] {}", response);
        assertThat(response).isNotNull();
        assertThat(response.numFound()).isEqualTo(1);
        assertThat(response.docs()).hasSize(1);
        assertThat(response.docs().get(0).doc().bookName()).isEqualTo("소년이 운다");
        assertThat(response.docs().get(0).doc().isbn13()).isEqualTo("9788936433840");
    }

    @Test
    @DisplayName("searchBooks - NaruErrorResponse 에러 응답")
    void searchBooks_error() {
        givenResponse(ERROR_JSON);

        assertThatThrownBy(() -> naruApiClient.searchBooks("소년이 운다"))
                .isInstanceOf(NaruApiException.class)
                .hasMessageContaining("authKey가 유효하지 않습니다.");

        log.info("[searchBooks 에러 응답] NaruApiException(errCode=003) 발생 확인");
    }

    // ===================== searchLibraries =====================

    @Test
    @DisplayName("searchLibraries - NaruApiResponse 정상 응답")
    void searchLibraries_success() {
        String json = """
                {
                  "response": {
                    "numFound": 1,
                    "resultNum": 1,
                    "libs": [
                      {
                        "lib": {
                          "libCode": "111004",
                          "libName": "서울특별시교육청어린이도서관",
                          "address": "서울특별시 종로구 사직로9길 7",
                          "tel": "02-736-5801",
                          "fax": "02-736-5801",
                          "latitude": "37.5767",
                          "longitude": "126.9742",
                          "homepage": "http://child.sen.go.kr",
                          "closed": "일요일",
                          "operatingTime": "09:00~18:00",
                          "BookCount": "50000"
                        }
                      }
                    ]
                  }
                }
                """;
        givenResponse(json);

        LibrarySearchResponse response = naruApiClient.searchLibraries("11");

        log.info("[searchLibraries 정상 응답] {}", response);
        assertThat(response).isNotNull();
        assertThat(response.numFound()).isEqualTo(1);
        assertThat(response.resultNum()).isEqualTo(1);
        assertThat(response.libs()).hasSize(1);
        assertThat(response.libs().get(0).lib().libCode()).isEqualTo("111004");
        assertThat(response.libs().get(0).lib().libName()).isEqualTo("서울특별시교육청어린이도서관");
    }

    @Test
    @DisplayName("searchLibraries - NaruErrorResponse 에러 응답")
    void searchLibraries_error() {
        givenResponse(ERROR_JSON);

        assertThatThrownBy(() -> naruApiClient.searchLibraries("11"))
                .isInstanceOf(NaruApiException.class)
                .hasMessageContaining("authKey가 유효하지 않습니다.");

        log.info("[searchLibraries 에러 응답] NaruApiException(errCode=003) 발생 확인");
    }

    // ===================== checkBookExists =====================

    @Test
    @DisplayName("checkBookExists - NaruApiResponse 정상 응답")
    void checkBookExists_success() {
        String json = """
                {
                  "response": {
                    "result": {
                      "hasBook": "Y",
                      "loanAvailable": "Y"
                    }
                  }
                }
                """;
        givenResponse(json);

        BookExistsResponse response = naruApiClient.checkBookExists("711618", "9788958284178");

        log.info("[checkBookExists 정상 응답] {}", response);
        assertThat(response).isNotNull();
        assertThat(response.result().hasBook()).isEqualTo("Y");
        assertThat(response.result().loanAvailable()).isEqualTo("Y");
    }

    @Test
    @DisplayName("checkBookExists - NaruErrorResponse 에러 응답")
    void checkBookExists_error() {
        givenResponse(ERROR_JSON);

        assertThatThrownBy(() -> naruApiClient.checkBookExists("711618", "9788958284178"))
                .isInstanceOf(NaruApiException.class)
                .hasMessageContaining("authKey가 유효하지 않습니다.");

        log.info("[checkBookExists 에러 응답] NaruApiException(errCode=003) 발생 확인");
    }

    // ===================== searchLibrariesByBooks =====================

    @Test
    @DisplayName("searchLibrariesByBooks - NaruApiResponse 정상 응답")
    void searchLibrariesByBooks_success() {
        String json = """
                {
                  "response": {
                    "numFound": 2,
                    "resultNum": 2,
                    "libs": [
                      {
                        "lib": {
                          "libCode": "111004",
                          "libName": "서울특별시교육청어린이도서관",
                          "address": "서울특별시 종로구 사직로9길 7",
                          "tel": "02-736-5801",
                          "fax": "02-736-5801",
                          "latitude": "37.5767",
                          "longitude": "126.9742",
                          "homepage": "http://child.sen.go.kr",
                          "closed": "일요일",
                          "operatingTime": "09:00~18:00"
                        }
                      },
                      {
                        "lib": {
                          "libCode": "111007",
                          "libName": "서울 종로도서관",
                          "address": "서울특별시 종로구 율곡로 176",
                          "tel": "02-2148-1691",
                          "fax": "02-2148-1699",
                          "latitude": "37.5796",
                          "longitude": "126.9820",
                          "homepage": "http://jongno.lib.seoul.kr",
                          "closed": "일요일",
                          "operatingTime": "09:00~21:00"
                        }
                      }
                    ]
                  }
                }
                """;
        givenResponse(json);

        LibrarySearchResponse response = naruApiClient.searchLibrariesByBooks("9788958284178", "11");

        log.info("[searchLibrariesByBooks 정상 응답] {}", response);
        assertThat(response).isNotNull();
        assertThat(response.numFound()).isEqualTo(2);
        assertThat(response.libs()).hasSize(2);
        assertThat(response.libs().get(0).lib().libCode()).isEqualTo("111004");
    }

    @Test
    @DisplayName("searchLibrariesByBooks - NaruErrorResponse 에러 응답")
    void searchLibrariesByBooks_error() {
        givenResponse(ERROR_JSON);

        assertThatThrownBy(() -> naruApiClient.searchLibrariesByBooks("9788958284178", "11"))
                .isInstanceOf(NaruApiException.class)
                .hasMessageContaining("authKey가 유효하지 않습니다.");

        log.info("[searchLibrariesByBooks 에러 응답] NaruApiException(errCode=003) 발생 확인");
    }

    // ===================== searchBookDetail =====================

    @Test
    @DisplayName("searchBookDetail - NaruApiResponse 정상 응답")
    void searchBookDetail_success() {
        String json = """
                {
                  "response": {
                    "detail": [
                      {
                        "book": {
                          "no": 1,
                          "bookname": "Clean Code (클린 코드)",
                          "authors": "로버트 C. 마틴 지음 ; 박재호, 이해영 옮김",
                          "publisher": "인사이트",
                          "publication_date": "2013-12-24",
                          "publication_year": "2013",
                          "isbn": "8960777331",
                          "isbn13": "9788960777330",
                          "addition_symbol": "",
                          "vol": "",
                          "class_no": "005.133",
                          "class_nm": "특정 프로그래밍 언어",
                          "description": "소프트웨어 장인 정신을 담은 책. 애자일 소프트웨어 장인 정신.",
                          "bookImageURL": "https://image.aladin.co.kr/product/27/49/cover200/8960777331_1.jpg"
                        }
                      }
                    ]
                  }
                }
                """;
        givenResponse(json);

        BookDetailResponse response = naruApiClient.searchBookDetail("9788960777330");

        log.info("[searchBookDetail 정상 응답] {}", response);
        assertThat(response).isNotNull();
        assertThat(response.detail()).hasSize(1);
        assertThat(response.detail().get(0).book().bookName()).isEqualTo("Clean Code (클린 코드)");
        assertThat(response.detail().get(0).book().isbn13()).isEqualTo("9788960777330");
        assertThat(response.detail().get(0).book().publisher()).isEqualTo("인사이트");
    }

    @Test
    @DisplayName("searchBookDetail - NaruErrorResponse 에러 응답")
    void searchBookDetail_error() {
        givenResponse(ERROR_JSON);

        assertThatThrownBy(() -> naruApiClient.searchBookDetail("9788960777330"))
                .isInstanceOf(NaruApiException.class)
                .hasMessageContaining("authKey가 유효하지 않습니다.");

        log.info("[searchBookDetail 에러 응답] NaruApiException(errCode=003) 발생 확인");
    }

    // ===================== loanItemsSearch =====================

    @Test
    @DisplayName("loanItemsSearch - NaruApiResponse 정상 응답")
    void loanItemsSearch_success() {
        String json = """
                {
                  "response": {
                    "resultNum": 1,
                    "numFound": 1,
                    "docs": [
                      {
                        "doc": {
                          "no": 1,
                          "ranking": "1",
                          "bookname": "아몬드",
                          "authors": "손원평 지음",
                          "publisher": "창비",
                          "publication_year": "2017",
                          "isbn13": "9788936434595",
                          "bookImageURL": "https://image.aladin.co.kr/product/11743/44/cover200/k412534027_1.jpg",
                          "loan_count": "500"
                        }
                      }
                    ]
                  }
                }
                """;
        givenResponse(json);

        LoanItemResponse response = naruApiClient.searchHotBooksByDateAndRegion("2024-01-01", "2024-12-31", "29");

        log.info("[loanItemsSearch 정상 응답] {}", response);
        assertThat(response).isNotNull();
        assertThat(response.resultNum()).isEqualTo(1);
        assertThat(response.numFound()).isEqualTo(1);
        assertThat(response.docs()).hasSize(1);
        assertThat(response.docs().get(0).doc().bookName()).isEqualTo("아몬드");
        assertThat(response.docs().get(0).doc().ranking()).isEqualTo("1");
        assertThat(response.docs().get(0).doc().loanCount()).isEqualTo("500");
    }

    @Test
    @DisplayName("loanItemsSearch - NaruErrorResponse 에러 응답")
    void loanItemsSearch_error() {
        givenResponse(ERROR_JSON);

        assertThatThrownBy(() -> naruApiClient.searchHotBooksByDateAndRegion("2024-01-01", "2024-12-31", "29"))
                .isInstanceOf(NaruApiException.class)
                .hasMessageContaining("authKey가 유효하지 않습니다.");

        log.info("[loanItemsSearch 에러 응답] NaruApiException(errCode=003) 발생 확인");
    }

    // ===================== searchHotTrendBooks =====================

    @Test
    @DisplayName("searchHotTrendBooks - NaruApiResponse 정상 응답")
    void searchHotTrendBooks_success() {
        String json = """
                {
                  "response": {
                    "results": [
                      {
                        "result": {
                          "date": "2024-01-01",
                          "docs": [
                            {
                              "doc": {
                                "no": 1,
                                "difference": 5,
                                "baseWeekRank": 1,
                                "pastWeekRank": 6,
                                "bookname": "트렌드 코리아 2024",
                                "authors": "김난도 외 지음",
                                "publisher": "미래의창",
                                "publication_year": "2023",
                                "isbn13": "9788959896004",
                                "bookImageURL": "https://image.aladin.co.kr/product/33096/70/cover200/k932930024_1.jpg"
                              }
                            }
                          ]
                        }
                      }
                    ]
                  }
                }
                """;
        givenResponse(json);

        HotTrendResponse response = naruApiClient.searchHotTrendBooks("2024-01-01");

        log.info("[searchHotTrendBooks 정상 응답] {}", response);
        assertThat(response).isNotNull();
        assertThat(response.results()).hasSize(1);
        assertThat(response.results().get(0).result().date()).isEqualTo("2024-01-01");
        assertThat(response.results().get(0).result().docs()).hasSize(1);

        HotTrendResponse.HotTrendItem item = response.results().get(0).result().docs().get(0).doc();
        assertThat(item.bookName()).isEqualTo("트렌드 코리아 2024");
        assertThat(item.baseWeekRank()).isEqualTo(1);
        assertThat(item.pastWeekRank()).isEqualTo(6);
        assertThat(item.difference()).isEqualTo(5);
    }

    @Test
    @DisplayName("searchHotTrendBooks - NaruErrorResponse 에러 응답")
    void searchHotTrendBooks_error() {
        givenResponse(ERROR_JSON);

        assertThatThrownBy(() -> naruApiClient.searchHotTrendBooks("2024-01-01"))
                .isInstanceOf(NaruApiException.class)
                .hasMessageContaining("authKey가 유효하지 않습니다.");

        log.info("[searchHotTrendBooks 에러 응답] NaruApiException(errCode=003) 발생 확인");
    }

    // ===================== searchRecommendBooks =====================

    @Test
    @DisplayName("searchRecommendBooks - NaruApiResponse 정상 응답 (mania)")
    void searchRecommendBooks_mania_success() {
        String json = """
                {
                  "response": {
                    "resultNum": 1,
                    "docs": [
                      {
                        "book": {
                          "no": 1,
                          "bookname": "리팩터링 2판",
                          "authors": "마틴 파울러 지음 ; 개앞맵시, 남기혁 옮김",
                          "publisher": "한빛미디어",
                          "publication_year": "2020",
                          "isbn13": "9791162242742",
                          "bookImageURL": "https://image.aladin.co.kr/product/22338/19/cover200/k622632750_1.jpg"
                        }
                      }
                    ]
                  }
                }
                """;
        givenResponse(json);

        RecommendResponse response = naruApiClient.searchRecommendBooks("9788960777330", "mania");

        log.info("[searchRecommendBooks mania 정상 응답] {}", response);
        assertThat(response).isNotNull();
        assertThat(response.resultNum()).isEqualTo(1);
        assertThat(response.docs()).hasSize(1);
        assertThat(response.docs().get(0).book().bookName()).isEqualTo("리팩터링 2판");
        assertThat(response.docs().get(0).book().isbn13()).isEqualTo("9791162242742");
    }

    @Test
    @DisplayName("searchRecommendBooks - NaruApiResponse 정상 응답 (reader)")
    void searchRecommendBooks_reader_success() {
        String json = """
                {
                  "response": {
                    "resultNum": 1,
                    "docs": [
                      {
                        "book": {
                          "no": 1,
                          "bookname": "이펙티브 자바",
                          "authors": "조슈아 블로크 지음 ; 개앞맵시 옮김",
                          "publisher": "인사이트",
                          "publication_year": "2018",
                          "isbn13": "9788966262281",
                          "bookImageURL": "https://image.aladin.co.kr/product/18477/72/cover200/k682530987_1.jpg"
                        }
                      }
                    ]
                  }
                }
                """;
        givenResponse(json);

        RecommendResponse response = naruApiClient.searchRecommendBooks("9788960777330", "reader");

        log.info("[searchRecommendBooks reader 정상 응답] {}", response);
        assertThat(response).isNotNull();
        assertThat(response.resultNum()).isEqualTo(1);
        assertThat(response.docs()).hasSize(1);
        assertThat(response.docs().get(0).book().bookName()).isEqualTo("이펙티브 자바");
        assertThat(response.docs().get(0).book().isbn13()).isEqualTo("9788966262281");
    }

    @Test
    @DisplayName("searchRecommendBooks - NaruErrorResponse 에러 응답")
    void searchRecommendBooks_error() {
        givenResponse(ERROR_JSON);

        assertThatThrownBy(() -> naruApiClient.searchRecommendBooks("9788960777330", "mania"))
                .isInstanceOf(NaruApiException.class)
                .hasMessageContaining("authKey가 유효하지 않습니다.");

        log.info("[searchRecommendBooks 에러 응답] NaruApiException(errCode=003) 발생 확인");
    }
}