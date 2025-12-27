package com.example.library.service;

import com.example.library.domain.factory.LoanFactory;
import com.example.library.domain.model.*;
import com.example.library.domain.spec.*;
import com.example.library.dto.loan.BorrowRequest;
import com.example.library.dto.loan.LoanResponse;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.NotFoundException;
import com.example.library.repository.BookCopyRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private static final int MAX_ACTIVE_LOANS = 5;

    private final UserRepository userRepository;
    private final BookCopyRepository bookCopyRepository;
    private final LoanRepository loanRepository;
    private final LoanFactory loanFactory;

    private final com.example.library.repository.FineRepository fineRepository;

    @Transactional
    public LoanResponse borrow(BorrowRequest req) {
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new NotFoundException("User not found: " + req.userId()));

        BookCopy copy = bookCopyRepository.findByBarcode(req.copyBarcode().trim())
                .orElseThrow(() -> new NotFoundException("Copy not found: " + req.copyBarcode()));

        long activeLoans = loanRepository.countByUserIdAndStatus(user.getId(), LoanStatus.ACTIVE)
                + loanRepository.countByUserIdAndStatus(user.getId(), LoanStatus.OVERDUE);

        long unpaidFines = fineRepository
                .findByUserIdAndStatus(user.getId(), FineStatus.UNPAID)
                .size();

        BorrowContext ctx = new BorrowContext(
                user,
                copy,
                activeLoans,
                MAX_ACTIVE_LOANS,
                unpaidFines
        );

        BorrowEligibility eligibility = new BorrowEligibility(List.of(
                new UserNotBlockedSpec(),
                new CopyAvailableSpec(),
                new MaxLoansSpec(),
                new NoUnpaidFinesSpec()
        ));



        try {
            eligibility.validate(ctx);
        } catch (IllegalStateException ex) {
            throw new BadRequestException(ex.getMessage());
        }

        // Apply state change on copy
        copy.markBorrowed();

        Instant now = Instant.now();
        Loan loan = loanFactory.create(user, copy, now);

        Loan saved = loanRepository.save(loan);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<com.example.library.dto.loan.LoanResponse> listUserLoans(
            Long userId, String status, org.springframework.data.domain.Pageable pageable) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found: " + userId);
        }

        org.springframework.data.domain.Page<Loan> page;
        if (status == null || status.isBlank()) {
            page = loanRepository.findByUserId(userId, pageable);
        } else {
            LoanStatus s;
            try {
                s = LoanStatus.valueOf(status.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new com.example.library.exception.BadRequestException("Invalid status: " + status);
            }
            page = loanRepository.findByUserIdAndStatus(userId, s, pageable);
        }

        return page.map(this::toResponse);
    }

    @Transactional
    public LoanResponse returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found: " + loanId));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new BadRequestException("Loan is already returned");
        }

        loan.returnNow(Instant.now());
        Loan saved = loanRepository.save(loan);
        return toResponse(saved);
    }

    private LoanResponse toResponse(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getUser().getId(),
                loan.getCopy().getId(),
                loan.getCopy().getBarcode(),
                loan.getBorrowedAt(),
                loan.getDueAt(),
                loan.getReturnedAt(),
                loan.getStatus().name()
        );
    }
}


