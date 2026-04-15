package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.dto.request.*;
import com.app.quantitymeasurement.dto.response.*;
import com.app.quantitymeasurement.entity.User;
import com.app.quantitymeasurement.enums.*;
import com.app.quantitymeasurement.repository.UserRepository;
import com.app.quantitymeasurement.security.UserPrincipal;
import com.app.quantitymeasurement.security.jwt.JwtTokenProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Tests for AuthenticationService
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private EmailService emailService;
    @Mock private Authentication authentication;

    @InjectMocks
    private AuthenticationService authService;

    private User user;
    private UserPrincipal principal;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .email("alice@example.com")
            .name("Alice")
            .password("hash")
            .provider(AuthProvider.LOCAL)
            .role(Role.USER)
            .build();

        principal = UserPrincipal.create(user);
    }

    // ===================== REGISTER =====================

    @Test
    void testRegister() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hash");
        when(userRepository.save(any())).thenReturn(user);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(jwtTokenProvider.generateToken(any())).thenReturn("token");

        AuthResponse res = authService.register(
            new RegisterRequest("alice@example.com", "Strong@123", "Alice"));

        assertEquals("token", res.getAccessToken());
        verify(emailService).sendRegistrationEmail(anyString(), anyString());
    }

    @Test
    void testRegister_DuplicateEmail() {
        when(userRepository.existsByEmail("dup@example.com")).thenReturn(true);

        ResponseStatusException ex = assertThrows(
            ResponseStatusException.class,
            () -> authService.register(
                new RegisterRequest("dup@example.com", "Strong@123", "Dup"))
        );

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_PasswordHashed() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hash");
        when(userRepository.save(any())).thenReturn(user);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(jwtTokenProvider.generateToken(any())).thenReturn("token");

        authService.register(new RegisterRequest("x@mail.com", "pass", "X"));

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        assertEquals("hash", captor.getValue().getPassword());
    }

    // ===================== LOGIN =====================

    @Test
    void testLogin() {
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        when(jwtTokenProvider.generateToken(any())).thenReturn("token");

        AuthResponse res = authService.login(
            new AuthRequest("alice@example.com", "Strong@123"));

        assertEquals("token", res.getAccessToken());
        verify(emailService).sendLoginNotificationEmail("alice@example.com");
    }

    @Test
    void testLogin_Invalid() {
        when(authenticationManager.authenticate(any()))
            .thenThrow(new BadCredentialsException("bad"));

        assertThrows(ResponseStatusException.class,
            () -> authService.login(new AuthRequest("a","b")));

        verify(emailService, never()).sendLoginNotificationEmail(anyString());
    }

    // ===================== FORGOT PASSWORD =====================

    @Test
    void testForgotPassword() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("hash");

        MessageResponse res = authService.forgotPassword(
            "alice@example.com", new ForgotPasswordRequest("new"));

        assertNotNull(res);
        verify(emailService).sendForgotPasswordEmail("alice@example.com");
    }

    @Test
    void testForgotPassword_NotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
            () -> authService.forgotPassword("x", new ForgotPasswordRequest("new")));

        verify(userRepository, never()).save(any());
    }

    // ===================== RESET PASSWORD =====================

    @Test
    void testResetPassword() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("hash");

        MessageResponse res = authService.resetPassword(
            "alice@example.com", "old", "new");

        assertNotNull(res);
        verify(emailService).sendPasswordResetEmail("alice@example.com");
    }

    @Test
    void testResetPassword_InvalidCurrent() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(ResponseStatusException.class,
            () -> authService.resetPassword("a","b","c"));

        verify(userRepository, never()).save(any());
    }
}