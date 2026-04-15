package com.app.quantitymeasurement.entity;

import com.app.quantitymeasurement.enums.AuthProvider;
import com.app.quantitymeasurement.enums.Role;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

// Tests for User entity behavior
class UserTest {

    // ===================== BUILDER =====================

    @Test
    void testBuilder_LocalUser() {
        User user = User.builder()
            .email("alice@example.com")
            .name("Alice")
            .password("hashedPwd")
            .provider(AuthProvider.LOCAL)
            .role(Role.USER)
            .build();

        assertEquals("alice@example.com", user.getEmail());
        assertEquals("Alice", user.getName());
        assertEquals("hashedPwd", user.getPassword());
        assertEquals(AuthProvider.LOCAL, user.getProvider());
        assertEquals(Role.USER, user.getRole());
        assertNull(user.getProviderId());
        assertNull(user.getImageUrl());
    }

    @Test
    void testBuilder_DefaultRole() {
        User user = User.builder()
            .email("bob@example.com")
            .provider(AuthProvider.LOCAL)
            .build();

        assertEquals(Role.USER, user.getRole());
    }

    @Test
    void testBuilder_GoogleUser() {
        User user = User.builder()
            .email("carol@gmail.com")
            .name("Carol")
            .provider(AuthProvider.GOOGLE)
            .providerId("google-sub-123")
            .imageUrl("https://lh3.google.com/photo.jpg")
            .role(Role.USER)
            .build();

        assertEquals(AuthProvider.GOOGLE, user.getProvider());
        assertEquals("google-sub-123", user.getProviderId());
        assertEquals("https://lh3.google.com/photo.jpg", user.getImageUrl());
        assertNull(user.getPassword());
    }

   

    // ===================== PREPERSIST =====================

    @Test
    void testPrePersist_SetsCreatedAt() {
        User user = User.builder()
            .email("d@example.com")
            .provider(AuthProvider.LOCAL)
            .build();

        assertNull(user.getCreatedAt());

        user.prePersist();

        assertNotNull(user.getCreatedAt());
        assertTrue(user.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testPrePersist_UpdateTimestamp() throws InterruptedException {
        User user = User.builder()
            .email("e@example.com")
            .provider(AuthProvider.LOCAL)
            .build();

        user.prePersist();
        LocalDateTime first = user.getCreatedAt();

        Thread.sleep(10);
        user.prePersist();

        assertTrue(user.getCreatedAt().isAfter(first) || user.getCreatedAt().isEqual(first));
    }

    // ===================== toString =====================

    @Test
    void testToString_NoPasswordLeak() {
        User user = User.builder()
            .email("f@example.com")
            .password("$2a$10$secretHash")
            .provider(AuthProvider.LOCAL)
            .build();

        String str = user.toString();

        assertFalse(str.contains("secretHash"));
        assertTrue(str.contains("f@example.com"));
    }

    @Test
    void testToString_ContainsFields() {
        User user = User.builder()
            .email("g@example.com")
            .name("Greg")
            .provider(AuthProvider.GOOGLE)
            .role(Role.ADMIN)
            .build();

        String str = user.toString();

        assertTrue(str.contains("g@example.com"));
        assertTrue(str.contains("ADMIN"));
        assertTrue(str.contains("GOOGLE"));
    }

    // ===================== ROLE =====================

    @Test
    void testSetRole() {
        User user = User.builder()
            .email("h@example.com")
            .provider(AuthProvider.LOCAL)
            .build();

        assertEquals(Role.USER, user.getRole());

        user.setRole(Role.ADMIN);
        assertEquals(Role.ADMIN, user.getRole());
    }

    // ===================== DEFAULT CONSTRUCTOR =====================

    @Test
    void testNoArgsConstructor() {
        User user = new User();

        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getProvider());
    }
}