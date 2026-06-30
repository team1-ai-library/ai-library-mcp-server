package com.nhnacademy.data4library.dto.response;

// bookExist 전용
public record BookExistsInfo(
        boolean hasBook,
        boolean loanAvailable
) {
}