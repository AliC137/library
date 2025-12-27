package com.example.library.dto.user;

public record UserResponse(
        Long id,
        String name,
        String email,
        String role,
        String status
) {}
