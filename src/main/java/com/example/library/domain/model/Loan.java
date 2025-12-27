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
@Table(name = "loans",
        indexes = {
                @Index(name = "idx_loans_user_status", columnList = "user_id,status"),
                @Index(name = "idx_loans_copy_status", columnList = "copy_id,status")
        })
public class Loan extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "copy_id", nullable = false)
    private BookCopy copy;

    @Column(nullable = false)
    private Instant borrowedAt;

    @Column(nullable = false)
    private Instant dueAt;

    private Instant returnedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    // Domain behavior (OOP)
    public boolean isActive() {
        return status == LoanStatus.ACTIVE || status == LoanStatus.OVERDUE;
    }

    public void returnNow(Instant now) {
        if (status == LoanStatus.RETURNED) return;
        this.returnedAt = now;
        this.status = LoanStatus.RETURNED;
        this.copy.markAvailable();
    }

    public void markOverdue() {
        if (status == LoanStatus.ACTIVE) {
            this.status = LoanStatus.OVERDUE;
        }
    }
}
