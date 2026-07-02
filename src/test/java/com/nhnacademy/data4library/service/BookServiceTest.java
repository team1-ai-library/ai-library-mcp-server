package com.nhnacademy.data4library.service;

import com.nhnacademy.data4library.api.NaruApiClient;
import com.nhnacademy.data4library.dto.naru.book.*;
import com.nhnacademy.data4library.dto.naru.common.BookWrapped;
import com.nhnacademy.data4library.dto.naru.common.DocWrapped;
import com.nhnacademy.data4library.dto.naru.common.NaruBook;
import com.nhnacademy.data4library.dto.response.BookDetailInfo;
import com.nhnacademy.data4library.dto.response.BookInfo;
import com.nhnacademy.data4library.dto.response.HotTrendBookInfo;
import com.nhnacademy.data4library.dto.response.LoanBookInfo;
import com.nhnacademy.data4library.exception.NaruApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private NaruApiClient naruApiClient;

    @InjectMocks
    private BookService bookService;

    @Test
    void searchBook_success() {
        NaruBook naruBook = new NaruBook(
                "토비의 스프링", "이일민", "에이콘", "2015", "9788960777330", null, null, null, null, null, null, "100"
        );
        BookSearchResponse response = new BookSearchResponse(1, List.of(new DocWrapped<>(naruBook)));
        given(naruApiClient.searchBooks("토비의 스프링")).willReturn(response);
        List<BookInfo> result = bookService.searchBooks("토비의 스프링");
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().isbn13()).isEqualTo("9788960777330");
        assertThat(result.getFirst().bookName()).isEqualTo("토비의 스프링");
    }

    @Test
    void searchBookDetail_success() {
        BookDetailResponse.BookDetail detailResponse = new BookDetailResponse.BookDetail(
                1, "토비의 스프링", "이일민", "에이콘", "2015-01-01", "2015", "8960777331", "9788960777330", null, null, null, null, "스프링 입문서", null
        );
        BookDetailResponse response = new BookDetailResponse(List.of(new BookWrapped<>(detailResponse)));
        given(naruApiClient.searchBookDetail("9788960777330")).willReturn(response);
        BookDetailInfo result = bookService.searchBookDetail("9788960777330");
        assertAll(
                () -> assertThat(result.isbn13()).isEqualTo("9788960777330"),
                () -> assertThat(result.bookName()).isEqualTo("토비의 스프링"),
                () -> assertThat(result.authors()).isEqualTo("이일민"),
                () -> assertThat(result.publisher()).isEqualTo("에이콘"),
                () -> assertThat(result.publicationYear()).isEqualTo("2015"),
                () -> assertThat(result.description()).isEqualTo("스프링 입문서")
        );
    }

    @Test
    void searchBookDetail_fail() {
        BookDetailResponse response = new BookDetailResponse(List.of());
        given(naruApiClient.searchBookDetail("9788960777330")).willReturn(response);
        assertThatThrownBy(() -> bookService.searchBookDetail("9788960777330"))
                .isInstanceOf(NaruApiException.class)
                .hasMessageContaining("도서 상세 정보를 찾을 수 없습니다.");
    }

    @Test
    void searchHotBooks_success() {
        LoanItemResponse.LoanItem loanItem = new LoanItemResponse.LoanItem(
                1, "1", "토비의 스프링", "이일민", "에이콘", "2015", "9788960777330", null, "150"
        );
        LoanItemResponse response = new LoanItemResponse(10, 1, List.of(new DocWrapped<>(loanItem)));
        given(naruApiClient.searchHotBooksByDateAndRegion(anyString(), anyString(), anyString())).willReturn(response);
        List<LoanBookInfo> result = bookService.searchHotBooks("오늘", "내일", "광주");
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().isbn13()).isEqualTo("9788960777330");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().bookName()).isEqualTo("토비의 스프링");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().authors()).isEqualTo("이일민");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().publisher()).isEqualTo("에이콘");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().loanCount()).isEqualTo("150");
                }
        );
    }


    @Test
    void searchHotTrendBooks_success() {
        HotTrendResponse.HotTrendItem item = new HotTrendResponse.HotTrendItem(
                1, 5, 1, 6, "토비의 스프링", "이일민", "에이콘", "2015", "9788960777330", null
        );
        HotTrendResponse.Result result = new HotTrendResponse.Result("2026-06-30", List.of(new HotTrendResponse.HotTrendItemWrapper(item)));
        HotTrendResponse response = new HotTrendResponse(List.of(new HotTrendResponse.ResultWrapper(result)));
        given(naruApiClient.searchHotTrendBooks(anyString())).willReturn(response);
        List<HotTrendBookInfo> resultList = bookService.searchHotTrendBooks("오늘");
        assertAll(
                () -> assertThat(resultList).hasSize(1),
                () -> {
                    assertNotNull(resultList);
                    assertThat(resultList.getFirst().isbn13()).isEqualTo("9788960777330");
                },
                ()-> {
                    assertNotNull(resultList);
                    assertThat(resultList.getFirst().bookName()).isEqualTo("토비의 스프링");
                },
                () -> {
                    assertNotNull(resultList);
                    assertThat(resultList.getFirst().baseWeekRank()).isEqualTo(1);
                }
        );
    }

    @Test
    void searchHotTreadBooks_fail() {
        HotTrendResponse response = new HotTrendResponse(List.of());
        given(naruApiClient.searchHotTrendBooks(anyString())).willReturn(response);
        assertThatThrownBy(() -> bookService.searchHotTrendBooks("2026-06-30"))
                .isInstanceOf(NaruApiException.class)
                .hasMessageContaining("해당 날짜의 급상승 도서 결과가 없습니다");
    }

    @Test
    void searchRecommendBooks_success() {
        RecommendResponse.RecommendItem recommendItem = new RecommendResponse.RecommendItem(
                1, "토비의 스프링", "이일민", "에이콘", "2015", "9788960777330", null
        );
        RecommendResponse recommendResponse = new RecommendResponse(1, List.of(new BookWrapped<>(recommendItem)));
        given(naruApiClient.searchRecommendBooks(anyString(), anyString())).willReturn(recommendResponse);
        List<BookInfo> result = bookService.searchRecommendBooks("9788960777330", "mania");
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().isbn13()).isEqualTo("9788960777330");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().bookName()).isEqualTo("토비의 스프링");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().authors()).isEqualTo("이일민");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().publisher()).isEqualTo("에이콘");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().publicationYear()).isEqualTo("2015");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().loanCount()).isNull();
                }
        );
    }
}