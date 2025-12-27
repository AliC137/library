package com.example.library.dto.book;

import java.util.List;

public record BookResponse(
        Long id,
        String isbn,
        String title,
        String author,
        String category,
        String status,
        int totalCopies,
        int availableCopies
) {}
