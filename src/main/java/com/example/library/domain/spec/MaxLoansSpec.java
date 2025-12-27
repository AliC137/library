package com.example.library.domain.spec;

public class MaxLoansSpec implements Specification<BorrowContext> {
    @Override public boolean isSatisfiedBy(BorrowContext c) {
        return c.activeLoansCount() < c.maxLoansAllowed();
    }
    @Override public String message() { return "User has reached the maximum active loans limit"; }
}
