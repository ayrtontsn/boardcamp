package com.boardcamp.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GamesDTO {

    @NotBlank(message = "Field name cannot be null")
    @Size(min = 3, max = 150, message = "Length for name is 3 t0 150 characters!")
    private String name;

    private String image;

    @Positive(message = "stockTotal should be greater than zero")
    @NotNull(message = "Field stockTotal cannot be null")
    private Integer stockTotal;

    @Positive(message = "pricePerDay should be greater than zero")
    @NotNull(message = "Field pricePerDay cannot be null")
    private Double pricePerDay;
}
