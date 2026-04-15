package com.app.quantitymeasurement.entity;

import com.app.quantitymeasurement.enums.AuthProvider;
import com.app.quantitymeasurement.enums.Role;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User email (unique login identifier)
    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    // User name
    @Size(max = 100)
    private String name;

    // Password (null for OAuth users)
    private String password;

    // Auth provider (LOCAL / GOOGLE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    // OAuth provider ID (for Google users)
    private String providerId;

    // User role (default USER)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    // Profile image URL
    private String imageUrl;

    // Created timestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Set createdAt before saving
    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // Custom toString (exclude password)
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", provider=" + provider +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }
}