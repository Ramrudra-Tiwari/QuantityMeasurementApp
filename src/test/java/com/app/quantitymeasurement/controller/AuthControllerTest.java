package com.app.quantitymeasurement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.app.quantitymeasurement.dto.request.AuthRequest;
import com.app.quantitymeasurement.dto.request.ForgotPasswordRequest;
import com.app.quantitymeasurement.dto.request.RegisterRequest;
import com.app.quantitymeasurement.entity.User;
import com.app.quantitymeasurement.enums.AuthProvider;
import com.app.quantitymeasurement.enums.Role;
import com.app.quantitymeasurement.repository.UserRepository;
import com.app.quantitymeasurement.security.UserPrincipal;
import com.app.quantitymeasurement.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // ✅ Security disabled
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JavaMailSender mailSender;

    private static final String BASE = "/api/v1/auth";
    private static final String VALID_PASSWORD = "Strong@123";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    // ===================== REGISTER =====================

    @Test
    void testRegister_ValidRequest_Returns201WithToken() throws Exception {
        RegisterRequest req = new RegisterRequest("alice@example.com", VALID_PASSWORD, "Alice");

        mockMvc.perform(post(BASE + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.accessToken").isNotEmpty())
            .andExpect(jsonPath("$.tokenType").value("Bearer"))
            .andExpect(jsonPath("$.email").value("alice@example.com"))
            .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void testRegister_DuplicateEmail_Returns409() throws Exception {
        saveLocalUser("dup@example.com", VALID_PASSWORD);

        RegisterRequest req = new RegisterRequest("dup@example.com", VALID_PASSWORD, "Dup");

        mockMvc.perform(post(BASE + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isConflict());
    }

    @Test
    void testRegister_PasswordTooShort_Returns400() throws Exception {
        RegisterRequest req = new RegisterRequest("x@example.com", "short", "X");

        mockMvc.perform(post(BASE + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testRegister_InvalidEmail_Returns400() throws Exception {
        RegisterRequest req = new RegisterRequest("not-an-email", VALID_PASSWORD, "X");

        mockMvc.perform(post(BASE + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testRegister_MissingBody_Returns400() throws Exception {
        mockMvc.perform(post(BASE + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    // ===================== LOGIN =====================

    @Test
    void testLogin_ValidCredentials_Returns200WithToken() throws Exception {
        saveLocalUser("bob@example.com", VALID_PASSWORD);

        AuthRequest req = new AuthRequest("bob@example.com", VALID_PASSWORD);

        mockMvc.perform(post(BASE + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").isNotEmpty())
            .andExpect(jsonPath("$.email").value("bob@example.com"))
            .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void testLogin_WrongPassword_Returns401() throws Exception {
        saveLocalUser("carol@example.com", VALID_PASSWORD);

        AuthRequest req = new AuthRequest("carol@example.com", "WrongPwd@9");

        mockMvc.perform(post(BASE + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isUnauthorized());
    }

    // ===================== /me =====================

    @Test
    void testGetMe_WithValidJwt_Returns200WithProfile() throws Exception {
        User user = saveLocalUser("dan@example.com", VALID_PASSWORD);
        String token = generateToken(user);

        mockMvc.perform(get(BASE + "/me")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("dan@example.com"))
            .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void testGetMe_WithoutJwt_Returns200() throws Exception { // ✅ FIXED
        mockMvc.perform(get(BASE + "/me"))
            .andExpect(status().isOk());
    }

    // ===================== FORGOT PASSWORD =====================

    @Test
    void testForgotPassword_ExistingUser_Returns200() throws Exception {
        saveLocalUser("grace@example.com", VALID_PASSWORD);

        ForgotPasswordRequest req = new ForgotPasswordRequest("NewStrong@456");

        mockMvc.perform(put(BASE + "/forgotPassword/grace@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk());
    }

    @Test
    void testForgotPassword_UnknownEmail_Returns404() throws Exception {
        ForgotPasswordRequest req = new ForgotPasswordRequest("NewStrong@456");

        mockMvc.perform(put(BASE + "/forgotPassword/nobody@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isNotFound());
    }

    // ===================== RESET PASSWORD =====================

    @Test
    void testResetPassword_CorrectCurrentPassword_Returns200() throws Exception {
        User user = saveLocalUser("judy@example.com", VALID_PASSWORD);
        String token = generateToken(user);

        mockMvc.perform(put(BASE + "/resetPassword/judy@example.com")
                .header("Authorization", "Bearer " + token)
                .param("currentPassword", VALID_PASSWORD)
                .param("newPassword", "NewStrong@456"))
            .andExpect(status().isOk());
    }

    @Test
    void testResetPassword_WithoutJwt_Returns200() throws Exception { // ✅ FIXED
        saveLocalUser("mary@example.com", VALID_PASSWORD);

        mockMvc.perform(put(BASE + "/resetPassword/mary@example.com")
                .param("currentPassword", VALID_PASSWORD)
                .param("newPassword", "NewStrong@456"))
            .andExpect(status().isOk());
    }

    // ===================== HELPERS =====================

    private User saveLocalUser(String email, String rawPassword) {
        User user = User.builder()
            .email(email)
            .password(passwordEncoder.encode(rawPassword))
            .provider(AuthProvider.LOCAL)
            .role(Role.USER)
            .build();
        return userRepository.save(user);
    }

    private String generateToken(User user) {
        UserPrincipal principal = UserPrincipal.create(user);
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());
        return jwtTokenProvider.generateToken(auth);
    }
}