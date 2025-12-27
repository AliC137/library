package com.example.library.dto.fine;

import java.math.BigDecimal;
import java.time.Instant;

public record FineResponse(
        Long id,
        Long userId,
        Long loanId,
        BigDecimal amount,
        String status,
        Instant updatedAt
) {}
