package com.app.quantitymeasurement.integration;

import com.app.quantitymeasurement.dto.request.*;
import com.app.quantitymeasurement.dto.response.*;
import com.app.quantitymeasurement.entity.*;
import com.app.quantitymeasurement.enums.*;
import com.app.quantitymeasurement.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// Full integration tests (controller → service → repository)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class QuantityMeasurementApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private QuantityMeasurementRepository repository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private String baseUrl;
    private String authUrl;
    private String testToken;
    private String adminToken;

    private QuantityDTO feetDTO;
    private QuantityDTO inchesDTO;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/quantities";
        authUrl = "http://localhost:" + port + "/api/v1/auth";

        repository.deleteAll();
        userRepository.deleteAll();

        testToken = registerAndGetToken("test@example.com", "Strong@123");
        adminToken = registerAdminAndGetToken("admin@example.com", "Strong@123");

        feetDTO = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        inchesDTO = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);
    }

    // ===================== BASIC =====================

    @Test
    void testApplicationStarts() {
        assertNotNull(restTemplate);
        assertNotNull(repository);
    }

    // ===================== COMPARE =====================

    @Test
    void testCompare() {
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, null);

        ResponseEntity<QuantityMeasurementDTO> response =
            restTemplate.exchange(baseUrl + "/compare", HttpMethod.POST,
                withToken(input, testToken), QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("true", response.getBody().getResultString());
    }

    // ===================== CONVERT =====================

    @Test
    void testConvert() {
        QuantityDTO target = new QuantityDTO(0.0, QuantityDTO.LengthUnit.INCHES);
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, target, null);

        ResponseEntity<QuantityMeasurementDTO> response =
            restTemplate.exchange(baseUrl + "/convert", HttpMethod.POST,
                withToken(input, testToken), QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(12.0, response.getBody().getResultValue(), 1e-4);
    }

    // ===================== ADD =====================

    @Test
    void testAdd() {
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, null);

        ResponseEntity<QuantityMeasurementDTO> response =
            restTemplate.exchange(baseUrl + "/add", HttpMethod.POST,
                withToken(input, testToken), QuantityMeasurementDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2.0, response.getBody().getResultValue(), 1e-4);
    }

    // ===================== INVALID =====================

    @Test
    void testInvalidUnit_Returns400() {
        String badJson = "{\"thisQuantityDTO\":{\"value\":1.0,\"unit\":\"FOOT\",\"measurementType\":\"LengthUnit\"}}";

        HttpEntity<String> entity = new HttpEntity<>(badJson, new HttpHeaders());

        ResponseEntity<Map> response =
            restTemplate.exchange(baseUrl + "/compare", HttpMethod.POST,
                withToken(entity, testToken), Map.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // ===================== HISTORY =====================

    @Test
    void testHistoryAfterCompare() {
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, null);

        restTemplate.exchange(baseUrl + "/compare", HttpMethod.POST,
            withToken(input, testToken), QuantityMeasurementDTO.class);

        ResponseEntity<List<QuantityMeasurementDTO>> response =
            restTemplate.exchange(baseUrl + "/history/operation/compare",
                HttpMethod.GET, withToken(testToken),
                new ParameterizedTypeReference<>() {});

        assertFalse(response.getBody().isEmpty());
    }

    // ===================== COUNT =====================

    @Test
    void testOperationCount() {
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, null);

        restTemplate.exchange(baseUrl + "/compare", HttpMethod.POST,
            withToken(input, testToken), QuantityMeasurementDTO.class);
        restTemplate.exchange(baseUrl + "/compare", HttpMethod.POST,
            withToken(input, testToken), QuantityMeasurementDTO.class);

        ResponseEntity<Long> response =
            restTemplate.exchange(baseUrl + "/count/compare",
                HttpMethod.GET, withToken(testToken), Long.class);

        assertEquals(2L, response.getBody());
    }

    // ===================== HEALTH =====================

    @Test
    void testHealth() {
        ResponseEntity<Map> response =
            restTemplate.getForEntity("http://localhost:" + port + "/actuator/health", Map.class);

        assertEquals("UP", response.getBody().get("status"));
    }

    // ===================== FLOW =====================

    @Test
    void testMultipleOperationsPersisted() {
        QuantityInputDTO input = new QuantityInputDTO(feetDTO, inchesDTO, null);

        restTemplate.exchange(baseUrl + "/compare", HttpMethod.POST,
            withToken(input, testToken), QuantityMeasurementDTO.class);

        restTemplate.exchange(baseUrl + "/add", HttpMethod.POST,
            withToken(input, testToken), QuantityMeasurementDTO.class);

        assertEquals(2, repository.findAll().size());
    }

    // ===================== AUTH HELPERS =====================

    private String registerAdminAndGetToken(String email, String password) {
        User admin = new User();
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setProvider(AuthProvider.LOCAL);
        admin.setRole(Role.ADMIN);

        userRepository.save(admin);

        AuthResponse resp = restTemplate.postForObject(
            authUrl + "/login",
            new AuthRequest(email, password),
            AuthResponse.class
        );

        return resp.getAccessToken();
    }

    private String registerAndGetToken(String email, String password) {
        AuthResponse resp = restTemplate.postForObject(
            authUrl + "/register",
            new RegisterRequest(email, password, "Test"),
            AuthResponse.class
        );

        return resp.getAccessToken();
    }

    private <T> HttpEntity<T> withToken(T body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    private HttpEntity<Void> withToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}