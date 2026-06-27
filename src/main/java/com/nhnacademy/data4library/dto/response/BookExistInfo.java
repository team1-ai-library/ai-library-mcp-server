package com.nhnacademy.data4library.dto.response;

// bookExist 전용
public record BookExistInfo(
        boolean hasBook,
        boolean loanAvailable

        // TODO
        //  "Y", "N" 문자열로 옴
        //  Service에서 변환해줘야 함
) {
}