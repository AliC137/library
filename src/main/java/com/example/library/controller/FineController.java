package com.example.library.controller;

import com.example.library.dto.fine.FineResponse;
import com.example.library.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    @GetMapping("/users/{userId}/fines")
    public java.util.List<FineResponse> list(@PathVariable Long userId) {
        return fineService.listUserFines(userId);
    }

    @PostMapping("/fines/{fineId}/pay")
    public FineResponse pay(@PathVariable Long fineId) {
        return fineService.pay(fineId);
    }
}
