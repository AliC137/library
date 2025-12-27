package com.example.library.controller;

import com.example.library.dto.book.BookCreateRequest;
import com.example.library.dto.book.BookResponse;
import com.example.library.dto.copy.BookCopyCreateRequest;
import com.example.library.dto.copy.BookCopyResponse;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public BookResponse create(@Valid @RequestBody BookCreateRequest req) {
        return bookService.createBook(req);
    }

    @GetMapping
    public Page<BookResponse> search(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String dir
    ) {
        Sort.Direction direction = dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return bookService.searchBooks(query, pageable);
    }

    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    @PostMapping("/{id}/copies")
    public BookCopyResponse addCopy(@PathVariable Long id, @Valid @RequestBody BookCopyCreateRequest req) {
        return bookService.addCopy(id, req);
    }

    @GetMapping("/{id}/copies")
    public List<BookCopyResponse> listCopies(@PathVariable Long id) {
        return bookService.listCopies(id);
    }
}
