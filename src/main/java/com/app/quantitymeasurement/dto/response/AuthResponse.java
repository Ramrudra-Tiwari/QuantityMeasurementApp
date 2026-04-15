package com.app.quantitymeasurement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    // JWT token (used in Authorization header)
    private String accessToken;

    // Token type (default: Bearer)
    @Builder.Default
    private String tokenType = "Bearer";

    // User details
    private String email;
    private String name;
    private String role;
}