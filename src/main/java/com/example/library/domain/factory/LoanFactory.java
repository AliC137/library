package com.example.library.domain.factory;

import com.example.library.domain.model.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class LoanFactory {

    private static final int LOAN_DAYS = 14;

    public Loan create(User user, BookCopy copy, Instant now) {
        return Loan.builder()
                .user(user)
                .copy(copy)
                .borrowedAt(now)
                .dueAt(now.plus(LOAN_DAYS, ChronoUnit.DAYS))
                .status(LoanStatus.ACTIVE)
                .build();
    }
}
