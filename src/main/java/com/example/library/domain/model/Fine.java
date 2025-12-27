package com.example.library.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "fines",
        indexes = {
                @Index(name = "idx_fines_user_status", columnList = "user_id,status"),
                @Index(name = "idx_fines_loan", columnList = "loan_id")
        })
public class Fine extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false, unique = true)
    private Loan loan;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FineStatus status;

    @Column(nullable = false)
    private Instant updatedAtBusiness;

    public boolean isUnpaid() {
        return status == FineStatus.UNPAID;
    }

    public void pay(Instant now) {
        this.status = FineStatus.PAID;
        this.updatedAtBusiness = now;
    }
}
