package com.app.quantitymeasurement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    // User email (must be valid)
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid address")
    private String email;

    // Password (8+ chars, uppercase, number, special char)
    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, max = 100, message = "Password must be 8-100 characters")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[@#$%^&*()+\\-=])(?=.*[0-9]).{8,}$",
        message = "Password must contain 1 uppercase, 1 special char, 1 number"
    )
    private String password;

    // Optional user name
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;
}