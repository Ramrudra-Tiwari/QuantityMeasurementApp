package com.app.quantitymeasurement.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.app.quantitymeasurement.dto.request.AuthRequest;
import com.app.quantitymeasurement.dto.request.ForgotPasswordRequest;
import com.app.quantitymeasurement.dto.request.RegisterRequest;
import com.app.quantitymeasurement.dto.response.AuthResponse;
import com.app.quantitymeasurement.dto.response.MessageResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

// DTO validation tests using Jakarta Validator (no Spring context)
class AuthDTOTest {

    private static Validator validator;
    private static final String VALID_PASSWORD = "Strong@123";

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    // ===================== AuthRequest =====================

    @Test
    void testAuthRequest_ValidPayload_NoViolations() {
        AuthRequest req = new AuthRequest("user@example.com", "anyPassword");
        assertTrue(validator.validate(req).isEmpty());
    }

    @Test
    void testAuthRequest_BlankEmail_Violation() {
        AuthRequest req = new AuthRequest("", "anyPassword");
        Set<String> messages = messages(validator.validate(req));
        assertTrue(messages.stream().anyMatch(m -> m.contains("blank")));
    }

    @Test
    void testAuthRequest_InvalidEmailFormat_Violation() {
        AuthRequest req = new AuthRequest("not-an-email", "anyPassword");
        Set<String> messages = messages(validator.validate(req));
        assertTrue(messages.stream().anyMatch(m -> m.toLowerCase().contains("valid")));
    }

    @Test
    void testAuthRequest_NullEmail_Violation() {
        AuthRequest req = new AuthRequest(null, "anyPassword");
        assertFalse(validator.validate(req).isEmpty());
    }

    @Test
    void testAuthRequest_BlankPassword_Violation() {
        AuthRequest req = new AuthRequest("user@example.com", "");
        Set<String> messages = messages(validator.validate(req));
        assertTrue(messages.stream().anyMatch(m -> m.contains("blank")));
    }

    @Test
    void testAuthRequest_NullPassword_Violation() {
        AuthRequest req = new AuthRequest("user@example.com", null);
        assertFalse(validator.validate(req).isEmpty());
    }

    // ===================== RegisterRequest =====================

    @Test
    void testRegisterRequest_ValidPayload_NoViolations() {
        RegisterRequest req = new RegisterRequest("new@example.com", VALID_PASSWORD, "Jane Doe");
        assertTrue(validator.validate(req).isEmpty());
    }

    @Test
    void testRegisterRequest_NullName_NoViolation() {
        RegisterRequest req = new RegisterRequest("new@example.com", VALID_PASSWORD, null);
        assertTrue(validator.validate(req).isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "short",
        "strong@123",
        "StrongPass1",
        "Strong@pass"
    })
    void testRegisterRequest_InvalidPasswords_Violation(String password) {
        RegisterRequest req = new RegisterRequest("x@example.com", password, "X");
        assertFalse(validator.validate(req).isEmpty());
    }

    @Test
    void testRegisterRequest_BlankEmail_Violation() {
        RegisterRequest req = new RegisterRequest("", VALID_PASSWORD, "Jane");
        assertFalse(validator.validate(req).isEmpty());
    }

    @Test
    void testRegisterRequest_InvalidEmail_Violation() {
        RegisterRequest req = new RegisterRequest("bad-email", VALID_PASSWORD, "Jane");
        assertFalse(validator.validate(req).isEmpty());
    }

    @Test
    void testRegisterRequest_PasswordTooLong_Violation() {
        String tooLong = "A@1" + "a".repeat(98);
        RegisterRequest req = new RegisterRequest("x@example.com", tooLong, "X");
        assertFalse(validator.validate(req).isEmpty());
    }

    @Test
    void testRegisterRequest_PasswordExactly8Chars_Valid() {
        RegisterRequest req = new RegisterRequest("x@example.com", "Strong@1", "X");
        assertTrue(validator.validate(req).isEmpty());
    }

    @Test
    void testRegisterRequest_PasswordExactly100Chars_Valid() {
        String exactly100 = "Aa1@" + "a".repeat(96);
        RegisterRequest req = new RegisterRequest("x@example.com", exactly100, "X");
        assertTrue(validator.validate(req).isEmpty());
    }

    @Test
    void testRegisterRequest_PasswordAllRequirementsMet_Valid() {
        RegisterRequest req = new RegisterRequest("x@example.com", "Strong@1x", "X");
        assertTrue(validator.validate(req).isEmpty());
    }

    @Test
    void testRegisterRequest_PasswordStrengthMessage() {
        RegisterRequest req = new RegisterRequest("x@example.com", "allowercase1@", "X");
        Set<String> messages = messages(validator.validate(req));

        assertTrue(messages.stream().anyMatch(m ->
            m.toLowerCase().contains("uppercase") ||
            m.toLowerCase().contains("special") ||
            m.toLowerCase().contains("number")
        ));
    }

    @Test
    void testRegisterRequest_NameTooLong_Violation() {
        String tooLongName = "a".repeat(101);
        RegisterRequest req = new RegisterRequest("x@example.com", VALID_PASSWORD, tooLongName);
        assertFalse(validator.validate(req).isEmpty());
    }

    @Test
    void testRegisterRequest_NameExactly100Chars_Valid() {
        String exactly100 = "a".repeat(100);
        RegisterRequest req = new RegisterRequest("x@example.com", VALID_PASSWORD, exactly100);
        assertTrue(validator.validate(req).isEmpty());
    }

    // ===================== ForgotPassword =====================

    @Test
    void testForgotPasswordRequest_ValidPassword() {
        ForgotPasswordRequest req = new ForgotPasswordRequest(VALID_PASSWORD);
        assertTrue(validator.validate(req).isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidPasswords")
    void testForgotPasswordRequest_InvalidPasswords(String password) {
        ForgotPasswordRequest req = new ForgotPasswordRequest(password);
        assertFalse(validator.validate(req).isEmpty());
    }

    private static Stream<String> invalidPasswords() {
        return Stream.of(
            null, "", "weak@123", "WeakPass1", "Weak@pass", "Str@ng1"
        );
    }

    @Test
    void testForgotPasswordRequest_StrongPassword() {
        ForgotPasswordRequest req = new ForgotPasswordRequest("NewStr@ng1");
        assertTrue(validator.validate(req).isEmpty());
    }

    // ===================== AuthResponse =====================

    @Test
    void testAuthResponse_Builder() {
        AuthResponse resp = AuthResponse.builder()
            .accessToken("token123")
            .tokenType("Bearer")
            .email("user@example.com")
            .name("User")
            .role("USER")
            .build();

        assertEquals("token123", resp.getAccessToken());
        assertEquals("Bearer", resp.getTokenType());
        assertEquals("user@example.com", resp.getEmail());
        assertEquals("User", resp.getName());
        assertEquals("USER", resp.getRole());
    }

    @Test
    void testAuthResponse_DefaultTokenType() {
        AuthResponse resp = AuthResponse.builder().build();
        assertEquals("Bearer", resp.getTokenType());
    }

    // ===================== MessageResponse =====================

    @Test
    void testMessageResponse_AllArgsConstructor() {
        MessageResponse resp = new MessageResponse("Password reset successfully!");
        assertEquals("Password reset successfully!", resp.getMessage());
    }

    @Test
    void testMessageResponse_NoArgsConstructor() {
        MessageResponse resp = new MessageResponse();
        assertNull(resp.getMessage());
    }

    @Test
    void testMessageResponse_Setter() {
        MessageResponse resp = new MessageResponse();
        resp.setMessage("Password has been changed successfully!");
        assertEquals("Password has been changed successfully!", resp.getMessage());
    }

    // ===================== Helper =====================

    private Set<String> messages(Set<? extends ConstraintViolation<?>> violations) {
        return violations.stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toSet());
    }
}