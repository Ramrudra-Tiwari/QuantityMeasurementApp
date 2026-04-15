package com.app.quantitymeasurement.security;

import com.app.quantitymeasurement.entity.User;
import com.app.quantitymeasurement.enums.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

// Tests for UserPrincipal
class UserPrincipalTest {

    private User localUser;
    private User googleUser;

    @BeforeEach
    void setUp() {
        localUser = User.builder()
            .id(1L).email("alice@example.com").name("Alice")
            .password("hash").provider(AuthProvider.LOCAL).role(Role.USER)
            .build();

        googleUser = User.builder()
            .id(2L).email("bob@gmail.com").name("Bob")
            .provider(AuthProvider.GOOGLE).providerId("sub")
            .role(Role.ADMIN).build();
    }

    // ===================== CREATE =====================

    @Test
    void testCreate() {
        UserPrincipal p = UserPrincipal.create(localUser);

        assertNotNull(p);
        assertEquals("alice@example.com", p.getUsername());
        assertEquals(localUser, p.getUser());
    }

    @Test
    void testCreate_WithAttributes() {
        Map<String, Object> attrs = Map.of("email", "bob@gmail.com");

        UserPrincipal p = UserPrincipal.create(googleUser, attrs);

        assertNotNull(p.getAttributes());
        assertEquals("bob@gmail.com", p.getAttributes().get("email"));
    }

    // ===================== IDENTITY =====================

    @Test
    void testIdentity() {
        UserPrincipal p = UserPrincipal.create(localUser);

        assertEquals("alice@example.com", p.getUsername());
        assertEquals("alice@example.com", p.getEmail());
        assertEquals(1L, p.getId());
    }

    // ===================== AUTHORITIES =====================

    @Test
    void testAuthorities() {
        Collection<? extends GrantedAuthority> userAuth =
            UserPrincipal.create(localUser).getAuthorities();

        Collection<? extends GrantedAuthority> adminAuth =
            UserPrincipal.create(googleUser).getAuthorities();

        assertEquals("ROLE_USER", userAuth.iterator().next().getAuthority());
        assertEquals("ROLE_ADMIN", adminAuth.iterator().next().getAuthority());
    }

    // ===================== PASSWORD =====================

    @Test
    void testPassword() {
        assertNotNull(UserPrincipal.create(localUser).getPassword());
        assertNull(UserPrincipal.create(googleUser).getPassword());
       }

    // ===================== FLAGS =====================

    @Test
    void testFlags() {
        UserPrincipal p = UserPrincipal.create(localUser);

        assertTrue(p.isAccountNonExpired());
        assertTrue(p.isAccountNonLocked());
        assertTrue(p.isCredentialsNonExpired());
        assertTrue(p.isEnabled());
    }

    // ===================== OAUTH =====================

    @Test
    void testGetName() {
        UserPrincipal p = UserPrincipal.create(googleUser, Map.of("email", "bob@gmail.com"));
        assertEquals("bob@gmail.com", p.getName());
    }

    @Test
    void testSetAttributes() {
        UserPrincipal p = UserPrincipal.create(googleUser);

        p.setAttributes(Map.of("sub", "123"));

        assertEquals("123", p.getAttributes().get("sub"));
    }
}