package com.example.library.service;

import com.example.library.domain.model.User;
import com.example.library.domain.model.UserRole;
import com.example.library.domain.model.UserStatus;
import com.example.library.dto.user.UserCreateRequest;
import com.example.library.dto.user.UserResponse;
import com.example.library.exception.BadRequestException;
import com.example.library.exception.NotFoundException;
import com.example.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse create(UserCreateRequest req) {
        if (userRepository.existsByEmail(req.email().trim())) {
            throw new BadRequestException("Email already exists: " + req.email());
        }

        User user = User.builder()
                .name(req.name().trim())
                .email(req.email().trim().toLowerCase())
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .build();

        return toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> list() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public UserResponse block(Long id) {
        User u = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
        u.block();
        return toResponse(userRepository.save(u));
    }

    @Transactional
    public UserResponse unblock(Long id) {
        User u = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
        u.unblock();
        return toResponse(userRepository.save(u));
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole().name(), u.getStatus().name());
    }
}
