package com.nhnacademy.data4library.service;

import com.nhnacademy.data4library.api.NaruApiClient;
import com.nhnacademy.data4library.dto.naru.common.LibWrapped;
import com.nhnacademy.data4library.dto.naru.common.NaruLibrary;
import com.nhnacademy.data4library.dto.naru.library.BookExistsResponse;
import com.nhnacademy.data4library.dto.naru.library.LibrarySearchResponse;
import com.nhnacademy.data4library.dto.response.BookExistsInfo;
import com.nhnacademy.data4library.dto.response.LibraryAvailabilityInfo;
import com.nhnacademy.data4library.dto.response.LibraryInfo;
import com.nhnacademy.data4library.exception.ErrorCode;
import com.nhnacademy.data4library.exception.NaruApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private NaruApiClient naruApiClient;

    @InjectMocks
    private LibraryService libraryService;

    @Test
    void searchLibraries_success() {
        NaruLibrary naruLibrary = new NaruLibrary(
                "129003", "계림꿈나무도서관", "광주광역시 동구 경양로247번길 26", "062-608-3920", "062-234-2293", "35.1585312", "126.9196578", "https://lib.donggu.kr", "매월 첫째, 셋째 월요일", "평일 09:00~18:00", "47214"
        );
        LibrarySearchResponse response = new LibrarySearchResponse(1, 1, List.of(new LibWrapped<>(naruLibrary)));
        given(naruApiClient.searchLibraries("24")).willReturn(response);
        List<LibraryInfo> result = libraryService.searchLibraries("광주");
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().libCode()).isEqualTo("129003");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().libName()).isEqualTo("계림꿈나무도서관");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().address()).isEqualTo("광주광역시 동구 경양로247번길 26");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().tel()).isEqualTo("062-608-3920");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().operatingTime()).isEqualTo("평일 09:00~18:00");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().homepage()).isEqualTo("https://lib.donggu.kr");
                }
        );
    }

    @Test
    void searchLibrariesByBooks_success() {
        NaruLibrary naruLibrary = new NaruLibrary(
                "129225", "광주 북구 양산도서관", "광주광역시 북구 하서로 299", "062-410-8242", null, "37.6036883", "127.0226004", "https://lib.bukgu.gwangju.kr", "매월 첫째주, 셋째주 월요일", "평일 09:00~18:00", null
        );
        LibrarySearchResponse response = new LibrarySearchResponse(1, 1, List.of(new LibWrapped<>(naruLibrary)));
        given(naruApiClient.searchLibrariesByBooks("9788960777330", "24")).willReturn(response);
        List<LibraryInfo> result = libraryService.searchLibrariesByBooks("9788960777330", "광주");
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().libCode()).isEqualTo("129225");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().libName()).isEqualTo("광주 북구 양산도서관");
                }
        );
    }

    @Test
    void checkBookExists_success() {
        BookExistsResponse.BookExistResult existResult = new BookExistsResponse.BookExistResult("Y", "Y");
        BookExistsResponse response = new BookExistsResponse(existResult);
        given(naruApiClient.checkBookExists("711618", "9788958284178")).willReturn(response);
        BookExistsInfo result = libraryService.checkBookExists("711618", "9788958284178");
        assertAll(
                () -> assertThat(result.hasBook()).isTrue(),
                () -> assertThat(result.loanAvailable()).isTrue()
        );
    }

    @Test
    void checkBookExists_fail() {
        BookExistsResponse.BookExistResult existResult = new BookExistsResponse.BookExistResult("Y", "X");
        BookExistsResponse response = new BookExistsResponse(existResult);
        given(naruApiClient.checkBookExists("711618", "9788958284178")).willReturn(response);
        BookExistsInfo result = libraryService.checkBookExists("711618", "9788958284178");
        assertAll(
                () -> assertThat(result.hasBook()).isTrue(),
                () -> assertThat(result.loanAvailable()).isFalse()
        );
    }

    @Test
    void checkBookExists_nonSearch() {
        BookExistsResponse response = new BookExistsResponse(null);
        given(naruApiClient.checkBookExists("711618", "9788958284178")).willReturn(response);
        assertThatThrownBy(() -> libraryService.checkBookExists("711618", "9788958284178"))
                .isInstanceOf(NaruApiException.class)
                .hasMessageContaining("소장 여부에 대한 정보를 찾을 수 없습니다.");
    }

    @Test
    void searchAvailableLibraries_success() {
        NaruLibrary naruLibrary = new NaruLibrary(
                "129225", "광주 북구 양산도서관", "광주광역시 북구 하서로 299", "062-410-8242", null, "37.6036883", "127.0226004", "https://lib.bukgu.gwangju.kr", "매월 첫째주, 셋째주 월요일", "평일 09:00~18:00", null
        );
        LibrarySearchResponse librarySearchResponse = new LibrarySearchResponse(1, 1, List.of(new LibWrapped<>(naruLibrary)));
        given(naruApiClient.searchLibrariesByBooks("9788960777330", "24")).willReturn(librarySearchResponse);
        BookExistsResponse.BookExistResult existResult = new BookExistsResponse.BookExistResult("Y", "Y");
        BookExistsResponse existsResponse = new BookExistsResponse(existResult);
        given(naruApiClient.checkBookExists("129225", "9788960777330")).willReturn(existsResponse);
        List<LibraryAvailabilityInfo> result = libraryService.searchAvailableLibraries("9788960777330", "광주");
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().libCode()).isEqualTo("129225");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().libName()).isEqualTo("광주 북구 양산도서관");
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().hasBook()).isTrue();
                },
                () -> {
                    assertNotNull(result);
                    assertThat(result.getFirst().loanAvailable()).isTrue();
                }
        );
    }

    @Test
    void searchAvailableLibraries_searchFail() {
        NaruLibrary naruLibrary = new NaruLibrary("129225", "광주 북구 양산도서관", "광주광역시 북구 하서로 299", "062-410-8242", null, "37.6036883", "127.0226004", "https://lib.bukgu.gwangju.kr", "매월 첫째주, 셋째주 월요일", "평일 09:00~18:00", null);
        LibrarySearchResponse librarySearchResponse = new LibrarySearchResponse(1, 1, List.of(new LibWrapped<>(naruLibrary)));

        given(naruApiClient.searchLibrariesByBooks("9788960777330", "24")).willReturn(librarySearchResponse);
        given(naruApiClient.checkBookExists("129225", "9788960777330")).willThrow(new NaruApiException(ErrorCode.NOT_FOUND, "오류"));

        List<LibraryAvailabilityInfo> result = libraryService.searchAvailableLibraries("9788960777330", "광주");

        assertThat(result).isEmpty();
    }
}