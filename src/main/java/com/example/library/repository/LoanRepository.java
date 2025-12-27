package com.example.library.repository;

import com.example.library.domain.model.Loan;
import com.example.library.domain.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    long countByUserIdAndStatus(Long userId, LoanStatus status);
    boolean existsByCopyIdAndStatus(Long copyId, LoanStatus status);

    Page<Loan> findByUserId(Long userId, Pageable pageable);
    Page<Loan> findByUserIdAndStatus(Long userId, LoanStatus status, Pageable pageable);
    List<Loan> findByStatusInAndDueAtBefore(List<LoanStatus> statuses, Instant cutoff);
}
