package com.example.library.repository;

import com.example.library.domain.model.Reservation;
import com.example.library.domain.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByUserIdAndBookIdAndStatus(Long userId, Long bookId, ReservationStatus status);

    List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Reservation> findByStatusAndExpiresAtBefore(ReservationStatus status, Instant cutoff);
}
