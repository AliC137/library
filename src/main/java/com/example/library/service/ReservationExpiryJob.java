package com.example.library.service;

import com.example.library.domain.model.Reservation;
import com.example.library.domain.model.ReservationStatus;
import com.example.library.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationExpiryJob {

    private final ReservationRepository reservationRepository;

    // every 5 minutes
    @Scheduled(fixedDelay = 300_000)
    @Transactional
    public void expireReservations() {
        Instant now = Instant.now();
        List<Reservation> expired = reservationRepository.findByStatusAndExpiresAtBefore(
                ReservationStatus.ACTIVE, now);

        for (Reservation r : expired) {
            r.expireIfNeeded(now);
        }
        reservationRepository.saveAll(expired);
    }
}
