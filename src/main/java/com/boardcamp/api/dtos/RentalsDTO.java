package com.boardcamp.api.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RentalsDTO {

    @NotNull
    @Positive
    private Long customerId;

    @NotNull
    @Positive
    private Long gameId;
    
    @NotNull
    @Positive
    private Long daysRented;
}
