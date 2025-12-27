package com.example.library.domain.spec;

public interface Specification<T> {
    boolean isSatisfiedBy(T candidate);
    String message();
}
