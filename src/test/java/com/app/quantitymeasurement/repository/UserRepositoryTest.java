package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.User;
import com.app.quantitymeasurement.enums.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// Repository tests for User
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    // ===================== EMAIL =====================

    @Test
    void testFindByEmail() {
        userRepository.save(localUser("alice@example.com", "Alice"));

        Optional<User> found = userRepository.findByEmail("alice@example.com");

        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getName());
    }

    @Test
    void testExistsByEmail() {
        userRepository.save(localUser("bob@example.com", "Bob"));

        assertTrue(userRepository.existsByEmail("bob@example.com"));
        assertFalse(userRepository.existsByEmail("none@example.com"));
    }

    // ===================== PROVIDER =====================

    @Test
    void testFindByProviderAndProviderId() {
        userRepository.save(googleUser("carol@gmail.com", "Carol", "id123"));

        Optional<User> found =
            userRepository.findByProviderAndProviderId(AuthProvider.GOOGLE, "id123");

        assertTrue(found.isPresent());
    }

    @Test
    void testProviderIsolation() {

        userRepository.save(googleUser("gg@example.com", "gg", "same-id"));

        assertTrue(userRepository
            .findByProviderAndProviderId(AuthProvider.GOOGLE, "same-id").isPresent());
    }

    // ===================== SAVE =====================

    @Test
    void testSave_DefaultRoleAndCreatedAt() {
        User saved = userRepository.save(localUser("f@example.com", "Frank"));

        assertNotNull(saved.getCreatedAt());
        assertEquals(Role.USER, saved.getRole());
    }

    @Test
    void testSave_AdminRole() {
        User admin = User.builder()
            .email("admin@example.com")
            .provider(AuthProvider.LOCAL)
            .role(Role.ADMIN)
            .build();

        User saved = userRepository.save(admin);

        assertEquals(Role.ADMIN,
            userRepository.findById(saved.getId()).get().getRole());
    }

    @Test
    void testUniqueEmailConstraint() {
        userRepository.save(localUser("dup@example.com", "First"));

        assertThrows(Exception.class,
            () -> userRepository.saveAndFlush(localUser("dup@example.com", "Second")));
    }

    // ===================== HELPERS =====================

    private User localUser(String email, String name) {
        return User.builder()
            .email(email)
            .name(name)
            .password("hash")
            .provider(AuthProvider.LOCAL)
            .role(Role.USER)
            .build();
    }



    private User googleUser(String email, String name, String id) {
        return User.builder()
            .email(email)
            .name(name)
            .provider(AuthProvider.GOOGLE)
            .providerId(id)
            .role(Role.USER)
            .build();
    }
}