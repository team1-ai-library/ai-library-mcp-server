package com.nhnacademy.data4library.agent;

import com.nhnacademy.data4library.api.NaruApiClient;
import com.nhnacademy.data4library.dto.BookExistBody;
import com.nhnacademy.data4library.dto.NaruApiResponse;
import com.nhnacademy.data4library.exception.NaruApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckBookExistService {

    private final NaruApiClient naruApiClient;

    public NaruApiResponse<BookExistBody> checkBookExists(String libCode, String isbn) {
        log.info("[CheckBookExistAgent] 에이전트 호출");

        NaruApiResponse<BookExistBody> result = this.naruApiClient.checkBookExists(libCode, isbn);

        log.info("[CheckBookExistAgent] API 응답 결과: {}", result);

        if (Objects.isNull(result) || Objects.isNull(result.response())) {
            throw new NaruApiException("NULL_RESPONSE_CHECK_BOOK_EXIST", "API 응답 결과가 null이거나 비어있습니다.");
        }

        // 널이 들어올 수 있는 것임
        String errorCode = result.response().errorCode();
        String error = result.response().error();

        if (Objects.nonNull(errorCode)) {
            throw new NaruApiException(errorCode, error);
        }

        return result;
    }
}
