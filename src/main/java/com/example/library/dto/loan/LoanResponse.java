package com.example.library.dto.loan;

import java.time.Instant;

public record LoanResponse(
        Long id,
        Long userId,
        Long copyId,
        String copyBarcode,
        Instant borrowedAt,
        Instant dueAt,
        Instant returnedAt,
        String status
) {}
