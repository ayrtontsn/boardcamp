package com.boardcamp.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomersDTO {
    @NotBlank(message = "Field name cannot be null")
    @Size(min = 3, max = 150, message = "Length for name is 3 t0 150 characters!")
    private String name;

    @NotBlank(message = "Field phone cannot be null")
    @Size(min = 10, max = 11, message = "Length for phone is 10 t0 11 numbers!")
    @Pattern(regexp = "^[\\d]*$", message = "Must contain only numbers!")
    private String phone;

    @NotBlank(message = "Field phone cannot be null")
    @Size(min = 11, max = 11, message = "Length for cpf is 11 numbers!")
    @Pattern(regexp = "^[\\d]*$", message = "Must contain only numbers!")
    private String cpf;
}
