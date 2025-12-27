package com.example.library.repository;

import com.example.library.domain.model.BookCopy;
import com.example.library.domain.model.BookCopyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    Optional<BookCopy> findByBarcode(String barcode);

    List<BookCopy> findByBookId(Long bookId);

    long countByBookIdAndStatus(Long bookId, com.example.library.domain.model.BookCopyStatus status);


    List<BookCopy> findByBookIdAndStatus(Long bookId, BookCopyStatus status);
}
