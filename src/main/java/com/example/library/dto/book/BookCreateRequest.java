package com.example.library.dto.book;

import jakarta.validation.constraints.NotBlank;

public record BookCreateRequest(
        @NotBlank String isbn,
        @NotBlank String title,
        @NotBlank String author,
        @NotBlank String category
) {}
