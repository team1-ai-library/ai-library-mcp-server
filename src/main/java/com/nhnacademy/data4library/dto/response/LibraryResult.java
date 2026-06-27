package com.nhnacademy.data4library.dto.response;

// libSrch, libSrchByBook 공통
public record LibraryResult(
        String libCode,
        String libName,
        String address,
        String tel,
        String operatingTime,
        String homepage
) {
}