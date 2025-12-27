package com.example.library.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "books",
        indexes = {
                @Index(name = "idx_books_isbn", columnList = "isbn"),
                @Index(name = "idx_books_title", columnList = "title")
        })
public class Book extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BookCopy> copies = new ArrayList<>();

    // Convenience method (keeps bidirectional relation consistent)
    public void addCopy(BookCopy copy) {
        copies.add(copy);
        copy.setBook(this);
    }
}
