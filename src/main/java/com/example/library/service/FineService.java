package com.example.library.service;

import com.example.library.domain.model.Fine;
import com.example.library.domain.model.FineStatus;
import com.example.library.dto.fine.FineResponse;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.NotFoundException;
import com.example.library.repository.FineRepository;
import com.example.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FineService {

    private final FineRepository fineRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<FineResponse> listUserFines(Long userId) {
        if (!userRepository.existsById(userId)) throw new NotFoundException("User not found: " + userId);
        return fineRepository.findByUserIdOrderByUpdatedAtBusinessDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public FineResponse pay(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new NotFoundException("Fine not found: " + fineId));

        if (fine.getStatus() == FineStatus.PAID) {
            throw new BadRequestException("Fine is already paid");
        }

        fine.pay(Instant.now());
        return toResponse(fineRepository.save(fine));
    }

    private FineResponse toResponse(Fine fine) {
        return new FineResponse(
                fine.getId(),
                fine.getUser().getId(),
                fine.getLoan().getId(),
                fine.getAmount(),
                fine.getStatus().name(),
                fine.getUpdatedAtBusiness()
        );
    }
}
