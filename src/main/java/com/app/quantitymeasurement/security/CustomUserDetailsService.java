package com.app.quantitymeasurement.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.quantitymeasurement.entity.User;
import com.app.quantitymeasurement.repository.UserRepository;

/**
 * Loads user details from database for authentication.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Used by Spring Security during login and JWT validation.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email: " + email));

        return UserPrincipal.create(user);
    }

    /**
     * Load user by ID (used internally when ID is already available).
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id)
            throws UsernameNotFoundException {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with id: " + id));

        return UserPrincipal.create(user);
    }
}