package com.example.library.dto.copy;

import jakarta.validation.constraints.NotBlank;

public record BookCopyCreateRequest(
        @NotBlank String barcode,
        @NotBlank String conditionNote
) {}
