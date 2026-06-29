package com.nhnacademy.data4library.dto.response;

public record LibraryAvailabilityInfo(
        String libCode,
        String libName,
        String address,
        String tel,
        String operatingTime,
        String homepage,
        boolean hasBook, // 소장 여부
        boolean loanAvailable // 대출 가능 여부
) {}