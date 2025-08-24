package com.example.cliente_service.clientes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RecuperarClaveReq(
    @NotBlank @Email @Size(max = 160) String email,
    @NotBlank @Size(max = 120) String token,
    @NotBlank @Size(max = 80) String nuevaClave
) {}

