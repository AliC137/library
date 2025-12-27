package com.example.library.dto.loan;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BorrowRequest(
        @NotNull Long userId,
        @NotBlank String copyBarcode
) {}
