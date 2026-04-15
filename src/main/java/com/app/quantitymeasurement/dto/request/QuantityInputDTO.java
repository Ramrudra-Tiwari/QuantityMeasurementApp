package com.app.quantitymeasurement.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import com.app.quantitymeasurement.dto.response.QuantityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityInputDTO {

    // First quantity (required)
    @NotNull(message = "thisQuantityDTO must not be null")
    @Valid
    private QuantityDTO thisQuantityDTO;

    // Second quantity (required)
    @NotNull(message = "thatQuantityDTO must not be null")
    @Valid
    private QuantityDTO thatQuantityDTO;

    // Optional target unit for result
    @Valid
    private QuantityDTO targetUnitDTO;
}