package com.app.quantitymeasurement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main entry point for the application.
 */
@SpringBootApplication
@EnableAsync
@OpenAPIDefinition(
    info = @Info(
        title = "Quantity Measurement API",
        version = "19.0",
        description = "API for quantity operations with JWT, OAuth2, and email support."
    )
)
public class QuantityMeasurementAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuantityMeasurementAppApplication.class, args);
    }
}