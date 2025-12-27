package com.example.library.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservations",
        indexes = {
                @Index(name = "idx_res_user_status", columnList = "user_id,status"),
                @Index(name = "idx_res_book_status", columnList = "book_id,status")
        })
public class Reservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    // Domain behavior
    public boolean isActive() {
        return status == ReservationStatus.ACTIVE;
    }

    public void cancel() {
        if (status == ReservationStatus.ACTIVE) {
            status = ReservationStatus.CANCELLED;
        }
    }

    public void expireIfNeeded(Instant now) {
        if (status == ReservationStatus.ACTIVE && now.isAfter(expiresAt)) {
            status = ReservationStatus.EXPIRED;
        }
    }

    public void fulfill() {
        if (status == ReservationStatus.ACTIVE) {
            status = ReservationStatus.FULFILLED;
        }
    }
}
