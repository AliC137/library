package com.example.library.repository;

import com.example.library.domain.model.Fine;
import com.example.library.domain.model.FineStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByUserIdOrderByUpdatedAtBusinessDesc(Long userId);
    List<Fine> findByUserIdAndStatus(Long userId, FineStatus status);
    Optional<Fine> findByLoanId(Long loanId);
}
