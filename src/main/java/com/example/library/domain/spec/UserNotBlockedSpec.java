package com.example.library.domain.spec;

public class UserNotBlockedSpec implements Specification<BorrowContext> {
    @Override public boolean isSatisfiedBy(BorrowContext c) { return !c.user().isBlocked(); }
    @Override public String message() { return "User is blocked"; }
}
