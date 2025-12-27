package com.example.library.domain.spec;

import com.example.library.domain.model.BookCopy;
import com.example.library.domain.model.User;

public record BorrowContext(
        User user,
        BookCopy copy,
        long activeLoansCount,
        int maxLoansAllowed,
        long unpaidFinesCount
) {}

