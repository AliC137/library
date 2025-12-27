package com.example.library.service;

import com.example.library.domain.model.*;
import com.example.library.dto.reservation.ReservationCreateRequest;
import com.example.library.dto.reservation.ReservationResponse;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.NotFoundException;
import com.example.library.repository.BookCopyRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.ReservationRepository;
import com.example.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private static final int RESERVATION_EXPIRE_DAYS = 2;

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationResponse reserve(ReservationCreateRequest req) {
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new NotFoundException("User not found: " + req.userId()));
        if (user.isBlocked()) throw new BadRequestException("User is blocked");

        Book book = bookRepository.findById(req.bookId())
                .orElseThrow(() -> new NotFoundException("Book not found: " + req.bookId()));
        if (book.getStatus() != BookStatus.ACTIVE) throw new BadRequestException("Book is not active");

        boolean alreadyReserved = reservationRepository.existsByUserIdAndBookIdAndStatus(
                user.getId(), book.getId(), ReservationStatus.ACTIVE);
        if (alreadyReserved) throw new BadRequestException("Active reservation already exists for this book");

        long available = bookCopyRepository.countByBookIdAndStatus(book.getId(), BookCopyStatus.AVAILABLE);
        if (available > 0) {
            throw new BadRequestException("Copies are available; borrow a copy instead of reserving");
        }

        Instant now = Instant.now();
        Reservation reservation = Reservation.builder()
                .user(user)
                .book(book)
                .createdAt(now)
                .expiresAt(now.plus(RESERVATION_EXPIRE_DAYS, ChronoUnit.DAYS))
                .status(ReservationStatus.ACTIVE)
                .build();

        Reservation saved = reservationRepository.save(reservation);
        return toResponse(saved);
    }

    @Transactional
    public ReservationResponse cancel(Long reservationId) {
        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found: " + reservationId));

        if (r.getStatus() != ReservationStatus.ACTIVE) {
            throw new BadRequestException("Only ACTIVE reservations can be cancelled");
        }

        r.cancel();
        return toResponse(reservationRepository.save(r));
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> listByUser(Long userId) {
        if (!userRepository.existsById(userId)) throw new NotFoundException("User not found: " + userId);
        return reservationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    private ReservationResponse toResponse(Reservation r) {
        return new ReservationResponse(
                r.getId(),
                r.getUser().getId(),
                r.getBook().getId(),
                r.getCreatedAt(),
                r.getExpiresAt(),
                r.getStatus().name()
        );
    }
}
