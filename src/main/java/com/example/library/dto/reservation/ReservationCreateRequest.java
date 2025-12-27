package com.example.library.dto.reservation;

import jakarta.validation.constraints.NotNull;

public record ReservationCreateRequest(
        @NotNull Long userId,
        @NotNull Long bookId
) {}
