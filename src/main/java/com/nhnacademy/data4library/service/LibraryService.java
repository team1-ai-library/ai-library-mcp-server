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
import java.util.concurrent.CompletableFuture;

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

    /**
     * 확인해야 할 도서관이 3곳이라면, 3곳을 동시에 확인함 -> 모두 완료 대기 -> 결과 수집
     * -> 각 도서관의 대출 가능 여부를 병렬로 확인
     * <p>
     * supplyAsync로 작업 던져놓고, join으로 결과 기다리는 것 (10개라면 10개를 동시에 던져놓고 병렬 실행됨)
     */
    public List<LibraryAvailabilityInfo> searchAvailableLibraries(String isbn13, String region) {

        log.info("[LibraryService] 대출 가능 도서관 검색 - isbn13: {}, region: {}", isbn13, region);

        // searchLibrariesByBooks(isbn, region) -> 소장 여부 확인
        // 이 지역에서 이 ISBN을 소장한 도서관 목록
        List<LibraryInfo> libs = this.searchLibrariesByBooks(isbn13, region);

        // 1. 각 도서관마다 '나중에 실행할 작업'을 만들어서 리스트에 담음
        List<CompletableFuture<LibraryAvailabilityInfo>> futures = new ArrayList<>();

        for (LibraryInfo lib : libs) {
            // supplyAsync: 백그라운드 스레드에서 비동기로 실행
            CompletableFuture<LibraryAvailabilityInfo> future = CompletableFuture.supplyAsync(() -> {
                // 이 블락이 백그라운드에서 실행됨
                try {
                    BookExistsInfo bookExistsInfo = this.checkBookExists(lib.libCode(), isbn13);

                    // 대출 불가능하면 널 리턴
                    if (!bookExistsInfo.loanAvailable()) {
                        return null;
                    }

                    return new LibraryAvailabilityInfo(
                            lib.libCode(),
                            lib.libName(),
                            lib.address(),
                            lib.tel(),
                            lib.operatingTime(),
                            lib.homepage(),
                            bookExistsInfo.hasBook(),
                            bookExistsInfo.loanAvailable()

                    );
                } catch (NaruApiException e) {
                    log.warn("[LibraryService] 소장 여부 확인 실패 - libCode: {}, 원인: {}", lib.libCode(), e.getMessage());
                    return null;
                }
            });

            futures.add(future); // 실행 예약만 하고 결과는 나중에 받음
        }

        // 모든 병렬 작업 완료 대기 후 널 제거
        List<LibraryAvailabilityInfo> result = new ArrayList<>();

        // 모든 작업이 끝날 때까지 기다리면서 결과 수집
        // join(): 이 작업 끝날 때까지 기다리라는 것
        for (CompletableFuture<LibraryAvailabilityInfo> future : futures) {
            LibraryAvailabilityInfo info = future.join(); // 완료될 떄까지 대기

            if (Objects.nonNull(info)) {
                result.add(info);
            }
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