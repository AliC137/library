package com.example.library.service;

import com.example.library.domain.model.*;
import com.example.library.dto.book.BookCreateRequest;
import com.example.library.dto.book.BookResponse;
import com.example.library.dto.copy.BookCopyCreateRequest;
import com.example.library.dto.copy.BookCopyResponse;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.NotFoundException;
import com.example.library.repository.BookCopyRepository;
import com.example.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    @Transactional
    public BookResponse createBook(BookCreateRequest req) {
        if (bookRepository.findByIsbn(req.isbn()).isPresent()) {
            throw new BadRequestException("Book with this ISBN already exists: " + req.isbn());
        }

        Book book = Book.builder()
                .isbn(req.isbn().trim())
                .title(req.title().trim())
                .author(req.author().trim())
                .category(req.category().trim())
                .status(BookStatus.ACTIVE)
                .build();

        Book saved = bookRepository.save(book);
        return toBookResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> searchBooks(String query, Pageable pageable) {
        Page<Book> page;
        if (query == null || query.isBlank()) {
            page = bookRepository.findAll(pageable);
        } else {
            page = bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                    query.trim(), query.trim(), pageable);
        }
        return page.map(this::toBookResponse);
    }

    @Transactional(readOnly = true)
    public BookResponse getBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));
        return toBookResponse(book);
    }

    @Transactional
    public BookCopyResponse addCopy(Long bookId, BookCopyCreateRequest req) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));

        if (bookCopyRepository.findByBarcode(req.barcode()).isPresent()) {
            throw new BadRequestException("Copy with this barcode already exists: " + req.barcode());
        }

        BookCopy copy = BookCopy.builder()
                .barcode(req.barcode().trim())
                .conditionNote(req.conditionNote().trim())
                .status(BookCopyStatus.AVAILABLE)
                .book(book)
                .build();

        BookCopy saved = bookCopyRepository.save(copy);
        return toCopyResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BookCopyResponse> listCopies(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new NotFoundException("Book not found: " + bookId);
        }
        return bookCopyRepository.findByBookId(bookId).stream()
                .map(this::toCopyResponse)
                .toList();
    }


    private BookResponse toBookResponse(Book book) {
        int total = book.getCopies() == null ? 0 : book.getCopies().size();
        int available = book.getCopies() == null ? 0 :
                (int) book.getCopies().stream().filter(c -> c.getStatus() == BookCopyStatus.AVAILABLE).count();

        return new BookResponse(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getStatus().name(),
                total,
                available
        );
    }

    private BookCopyResponse toCopyResponse(BookCopy copy) {
        return new BookCopyResponse(
                copy.getId(),
                copy.getBarcode(),
                copy.getConditionNote(),
                copy.getStatus().name()
        );
    }
}
