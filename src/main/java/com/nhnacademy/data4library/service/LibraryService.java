package com.nhnacademy.data4library.service;

import com.nhnacademy.data4library.api.NaruApiClient;
import com.nhnacademy.data4library.dto.naru.common.LibWrapped;
import com.nhnacademy.data4library.dto.naru.common.NaruLibrary;
import com.nhnacademy.data4library.dto.naru.library.BookExistsResponse;
import com.nhnacademy.data4library.dto.response.BookExistsInfo;
import com.nhnacademy.data4library.dto.response.LibraryAvailabilityInfo;
import com.nhnacademy.data4library.dto.response.LibraryInfo;
import com.nhnacademy.data4library.exception.ErrorCode;
import com.nhnacademy.data4library.exception.NaruApiException;
import com.nhnacademy.data4library.util.RegionCodeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    // ISBN과 지역으로 도서관 목록 획득
    public List<LibraryInfo> searchLibrariesByBooks(String isbn13, String region) {

        log.info("[LibraryService] 도서 소장 도서관 검색 - isbn13: {}, region: {}", isbn13, region);

        return naruApiClient.searchLibrariesByBooks(isbn13, RegionCodeConverter.convert(region))
                .libs()
                .stream()
                .map(this::toLibraryResult)
                .toList();
    }

    // 도서관 코드와 ISBN으로 해당 도서관 '대출 가능 여부' 확인
    public BookExistsInfo checkBookExists(String libCode, String isbn13) {

        log.info("[LibraryService] 도서 소장 여부 확인 - libCode: {}, isbn13: {}", libCode, isbn13);

        BookExistsResponse.BookExistResult result = this.naruApiClient.checkBookExists(libCode, isbn13).result();

        if (Objects.isNull(result)) {
            throw new NaruApiException(ErrorCode.NOT_FOUND, "소장 여부에 대한 정보를 찾을 수 없습니다.");
        }

        return new BookExistsInfo(
                "Y".equals(result.hasBook()),
                "Y".equals(result.loanAvailable())
        );
    }

    // 도서 소장 + 대출 가능 여부 한 번에
    // (도서관 수만큼 checkBookExists가 반복 호출되므로)
    // 내부에서 searchLibrariesByBooks + 각 도서관 checkBookExists 조합
    // 대출 가능한 도서관만 필터링해서 리턴
    public List<LibraryAvailabilityInfo> searchAvailableLibraries(String isbn13, String region) {

        log.info("[LibraryService] 대출 가능 도서관 검색 - isbn13: {}, region: {}", isbn13, region);

        // searchLibrariesByBooks(isbn, region) -> 소장 여부 확인
        // 이 지역에서 이 ISBN을 소장한 도서관 목록
        List<LibraryInfo> libs = this.searchLibrariesByBooks(isbn13, region);

        List<LibraryAvailabilityInfo> result = new ArrayList<>();

        // 각 도서관마다
        for (LibraryInfo lib : libs) {

            BookExistsInfo existsInfo;
            try {
                // 현재 대출 가능 여부
                // 소장은 하고 있어도 지금 모두 대출 중이면 대출 불가능할 수 있으므로.
                existsInfo = this.checkBookExists(lib.libCode(), isbn13);
            } catch (NaruApiException e) {
                log.warn("[LibraryService] 소장 여부 확인 실패 - libCode: {}, 원인: {}", lib.libCode(), e.getMessage());
                existsInfo = new BookExistsInfo(false, false);
            }

            result.add(new LibraryAvailabilityInfo(
                    lib.libCode(),
                    lib.libName(),
                    lib.address(),
                    lib.tel(),
                    lib.operatingTime(),
                    lib.homepage(),
                    existsInfo.hasBook(),
                    existsInfo.loanAvailable()
            ));
        }

        return result;
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