package com.app.quantitymeasurement.security;

import com.app.quantitymeasurement.entity.User;
import com.app.quantitymeasurement.enums.*;
import com.app.quantitymeasurement.security.jwt.JwtTokenProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

// Tests for JwtTokenProvider
@SpringBootTest
@ActiveProfiles("test")
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Authentication authentication;

    @BeforeEach
    void setUp() {
        User user = User.builder()
            .id(1L)
            .email("alice@example.com")
            .password("hash")
            .provider(AuthProvider.LOCAL)
            .role(Role.USER)
            .build();

        UserPrincipal principal = UserPrincipal.create(user);
        authentication = new UsernamePasswordAuthenticationToken(
            principal, null, principal.getAuthorities());
    }

    // ===================== GENERATE =====================

    @Test
    void testGenerateToken() {
        String token = jwtTokenProvider.generateToken(authentication);

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void testGenerateTokenFromEmail() {
        String token = jwtTokenProvider.generateTokenFromEmail("bob@example.com", "ROLE_USER");

        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals("bob@example.com", jwtTokenProvider.getEmailFromToken(token));
    }

    // ===================== CLAIMS =====================

    @Test
    void testExtractClaims() {
        String token = jwtTokenProvider.generateToken(authentication);

        assertEquals("alice@example.com", jwtTokenProvider.getEmailFromToken(token));
        assertTrue(jwtTokenProvider.getRolesFromToken(token).contains("ROLE_USER"));
    }

    // ===================== VALIDATION =====================

    @Test
    void testValidateToken_Valid() {
        String token = jwtTokenProvider.generateToken(authentication);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void testValidateToken_InvalidCases() {
        String token = jwtTokenProvider.generateToken(authentication);

        String tampered = token.substring(0, token.lastIndexOf('.') + 1) + "x";

        assertFalse(jwtTokenProvider.validateToken(tampered));
        assertFalse(jwtTokenProvider.validateToken("not.a.jwt"));
        assertFalse(jwtTokenProvider.validateToken(""));
        assertFalse(jwtTokenProvider.validateToken("header.payload"));
    }

    @Test
    void testValidateToken_Null() {
        try {
            assertFalse(jwtTokenProvider.validateToken(null));
        } catch (Exception ignored) {}
    }

    // ===================== MULTIPLE TOKENS =====================

    @Test
    void testMultipleTokensValid() {
        String t1 = jwtTokenProvider.generateToken(authentication);
        String t2 = jwtTokenProvider.generateToken(authentication);

        assertTrue(jwtTokenProvider.validateToken(t1));
        assertTrue(jwtTokenProvider.validateToken(t2));
    }
}