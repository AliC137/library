package com.example.library.controller;

import com.example.library.dto.reservation.ReservationCreateRequest;
import com.example.library.dto.reservation.ReservationResponse;
import com.example.library.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ReservationResponse reserve(@Valid @RequestBody ReservationCreateRequest req) {
        return reservationService.reserve(req);
    }

    @DeleteMapping("/{id}")
    public ReservationResponse cancel(@PathVariable Long id) {
        return reservationService.cancel(id);
    }

    @GetMapping("/users/{userId}")
    public List<ReservationResponse> listByUser(@PathVariable Long userId) {
        return reservationService.listByUser(userId);
    }
}
