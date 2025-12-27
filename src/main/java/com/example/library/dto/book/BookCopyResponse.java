package com.example.library.dto.copy;

public record BookCopyResponse(
        Long id,
        String barcode,
        String conditionNote,
        String status
) {}
