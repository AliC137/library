package com.example.library.controller;

import com.example.library.dto.loan.BorrowRequest;
import com.example.library.dto.loan.LoanResponse;
import com.example.library.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/borrow")
    public LoanResponse borrow(@Valid @RequestBody BorrowRequest req) {
        return loanService.borrow(req);
    }

    @PostMapping("/{loanId}/return")
    public LoanResponse returnLoan(@PathVariable Long loanId) {
        return loanService.returnLoan(loanId);
    }

    @GetMapping("/users/{userId}")
    public Page<LoanResponse> listUserLoans(
            @PathVariable Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String dir
    ) {
        Sort.Direction direction = dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return loanService.listUserLoans(userId, status, pageable);
    }
}
