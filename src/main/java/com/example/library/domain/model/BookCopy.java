package com.example.library.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "book_copies",
        indexes = {
                @Index(name = "idx_copies_barcode", columnList = "barcode")
        })
public class BookCopy extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String barcode;

    @Column(nullable = false)
    private String conditionNote; // e.g., "new", "good", "damaged cover"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookCopyStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Domain behavior
    public boolean isAvailable() {
        return this.status == BookCopyStatus.AVAILABLE;
    }

    public void markBorrowed() {
        this.status = BookCopyStatus.BORROWED;
    }

    public void markAvailable() {
        this.status = BookCopyStatus.AVAILABLE;
    }

    public void markLost() {
        this.status = BookCopyStatus.LOST;
    }
}
