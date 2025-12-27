package com.example.library.domain.spec;

public class CopyAvailableSpec implements Specification<BorrowContext> {
    @Override public boolean isSatisfiedBy(BorrowContext c) { return c.copy().isAvailable(); }
    @Override public String message() { return "Book copy is not available"; }
}
