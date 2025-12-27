package com.example.library.domain.spec;

import java.util.List;

public class BorrowEligibility {

    private final List<Specification<BorrowContext>> rules;

    public BorrowEligibility(List<Specification<BorrowContext>> rules) {
        this.rules = rules;
    }

    public void validate(BorrowContext ctx) {
        for (var rule : rules) {
            if (!rule.isSatisfiedBy(ctx)) {
                throw new IllegalStateException(rule.message());
            }
        }
    }
}
