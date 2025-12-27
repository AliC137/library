package com.example.library.service;

import com.example.library.domain.model.*;
import com.example.library.repository.FineRepository;
import com.example.library.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OverdueAndFineJob {

    private final LoanRepository loanRepository;
    private final FineRepository fineRepository;
    private final FineStrategy fineStrategy;

    // every 5 minutes
    @Scheduled(fixedDelay = 300_000)
    @Transactional
    public void scanOverduesAndUpdateFines() {
        Instant now = Instant.now();

        List<Loan> candidates = loanRepository.findByStatusInAndDueAtBefore(
                List.of(LoanStatus.ACTIVE, LoanStatus.OVERDUE),
                now
        );

        for (Loan loan : candidates) {
            // Mark overdue if needed
            loan.markOverdue();

            BigDecimal amount = fineStrategy.calculate(loan, now);

            // Create or update fine only if amount > 0
            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                Fine fine = fineRepository.findByLoanId(loan.getId())
                        .orElseGet(() -> Fine.builder()
                                .user(loan.getUser())
                                .loan(loan)
                                .amount(BigDecimal.ZERO)
                                .status(FineStatus.UNPAID)
                                .updatedAtBusiness(now)
                                .build());

                // If already paid, don't change it
                if (fine.getStatus() == FineStatus.UNPAID) {
                    fine.setAmount(amount);
                    fine.setUpdatedAtBusiness(now);
                    fineRepository.save(fine);
                }
            }
        }

        loanRepository.saveAll(candidates);
    }
}
