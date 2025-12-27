package com.example.library.dto.reservation;

import java.time.Instant;

public record ReservationResponse(
        Long id,
        Long userId,
        Long bookId,
        Instant createdAt,
        Instant expiresAt,
        String status
) {}
