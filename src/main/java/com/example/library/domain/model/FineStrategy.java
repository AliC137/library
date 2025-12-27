package com.example.library.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public interface FineStrategy {
    BigDecimal calculate(Loan loan, Instant now);
}
