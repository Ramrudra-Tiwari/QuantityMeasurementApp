package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.User;
import com.app.quantitymeasurement.enums.AuthProvider;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email
    Optional<User> findByEmail(String email);

    // Check if email already exists
    boolean existsByEmail(String email);

    // Find user by OAuth provider and provider ID
    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);
}