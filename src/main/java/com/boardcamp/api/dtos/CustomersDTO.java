package com.boardcamp.api.dtos;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomersDTO {
    @NotBlank(message = "Field name cannot be null")
    @Size(min = 3, max = 150, message = "Length for name is 3 t0 150 characters!")
    private String name;

    @NotBlank(message = "Field name cannot be null")
    @Size(min = 10, max = 11, message = "Length for phone is 10 t0 11 numbers!")
    @CPF
    private String phone;

    @Column(length = 11, nullable = false)
    @CPF
    private String cpf;
}
