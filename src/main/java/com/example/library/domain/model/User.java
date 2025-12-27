package com.example.library.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    // Domain behavior (OOP)
    public void block() {
        this.status = UserStatus.BLOCKED;
    }

    public void unblock() {
        this.status = UserStatus.ACTIVE;
    }

    public boolean isBlocked() {
        return this.status == UserStatus.BLOCKED;
    }
}
