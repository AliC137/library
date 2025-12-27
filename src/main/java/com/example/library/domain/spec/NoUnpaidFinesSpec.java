package com.example.library.domain.spec;

public class NoUnpaidFinesSpec implements Specification<BorrowContext> {
    @Override
    public boolean isSatisfiedBy(BorrowContext c) {
        return c.unpaidFinesCount() == 0;
    }

    @Override
    public String message() {
        return "User has unpaid fines";
    }
}
