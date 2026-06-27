package com.nhnacademy.data4library.service;

import com.nhnacademy.data4library.api.NaruApiClient;
import com.nhnacademy.data4library.dto.naru.common.LibWrapped;
import com.nhnacademy.data4library.dto.naru.common.NaruLibrary;
import com.nhnacademy.data4library.dto.naru.library.BookExistResponse;
import com.nhnacademy.data4library.dto.response.BookExistInfo;
import com.nhnacademy.data4library.dto.response.LibraryInfo;
import com.nhnacademy.data4library.exception.ErrorCode;
import com.nhnacademy.data4library.exception.NaruApiException;
import com.nhnacademy.data4library.util.RegionCodeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class LibraryService {

    private final NaruApiClient naruApiClient;

    public List<LibraryInfo> searchLibraries(String region) {

        log.info("[LibraryService] 도서관 검색 - region: {}", region);

        return this.naruApiClient.searchLibraries(RegionCodeConverter.convert(region))
                .libs()
                .stream()
                .map(this::toLibraryResult)
                .toList();
    }

    public List<LibraryInfo> searchLibrariesByBooks(String isbn13, String region) {

        log.info("[LibraryService] 도서 소장 도서관 검색 - isbn13: {}, region: {}", isbn13, region);

        return naruApiClient.searchLibrariesByBooks(isbn13, RegionCodeConverter.convert(region))
                .libs()
                .stream()
                .map(this::toLibraryResult)
                .toList();
    }

    public BookExistInfo checkBookExists(String libCode, String isbn13) {

        log.info("[LibraryService] 도서 소장 여부 확인 - libCode: {}, isbn13: {}", libCode, isbn13);

        BookExistResponse.BookExistResult result = this.naruApiClient.checkBookExists(libCode, isbn13).result();

        if (Objects.isNull(result)) {
            throw new NaruApiException(ErrorCode.NOT_FOUND, "소장 여부에 대한 정보를 찾을 수 없습니다.");
        }

        return new BookExistInfo(
                "Y".equals(result.hasBook()),
                "Y".equals(result.loanAvailable())
        );
    }

    private LibraryInfo toLibraryResult(LibWrapped<NaruLibrary> item) {

        return new LibraryInfo(
                item.lib().libCode(),
                item.lib().libName(),
                item.lib().address(),
                item.lib().tel(),
                item.lib().operatingTime(),
                item.lib().homepage()
        );
    }
}