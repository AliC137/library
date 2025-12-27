package com.example.library.controller;

import com.example.library.dto.user.UserCreateRequest;
import com.example.library.dto.user.UserResponse;
import com.example.library.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponse create(@Valid @RequestBody UserCreateRequest req) {
        return userService.create(req);
    }

    @GetMapping
    public List<UserResponse> list() {
        return userService.list();
    }

    @PostMapping("/{id}/block")
    public UserResponse block(@PathVariable Long id) {
        return userService.block(id);
    }

    @PostMapping("/{id}/unblock")
    public UserResponse unblock(@PathVariable Long id) {
        return userService.unblock(id);
    }
}
