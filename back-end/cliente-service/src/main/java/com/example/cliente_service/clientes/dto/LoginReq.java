package com.example.cliente_service.clientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginReq(
    @NotBlank @Email @Size(max = 160) String email,
    @NotBlank @Size(max = 80) String clave
) {}

