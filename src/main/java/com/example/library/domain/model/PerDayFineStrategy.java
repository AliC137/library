package com.example.library.domain.model;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

@Component
public class PerDayFineStrategy implements FineStrategy {

    private static final BigDecimal PER_DAY = new BigDecimal("1.00");

    @Override
    public BigDecimal calculate(Loan loan, Instant now) {
        if (loan.getStatus() == LoanStatus.RETURNED) return BigDecimal.ZERO;
        if (!now.isAfter(loan.getDueAt())) return BigDecimal.ZERO;

        long daysLate = Duration.between(loan.getDueAt(), now).toDays();
        if (daysLate <= 0) return BigDecimal.ZERO;

        return PER_DAY.multiply(BigDecimal.valueOf(daysLate));
    }
}
