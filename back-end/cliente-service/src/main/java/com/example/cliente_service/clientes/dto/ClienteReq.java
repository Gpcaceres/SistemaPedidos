package com.example.cliente_service.clientes.dto;
import jakarta.validation.constraints.*;
public record ClienteReq(
  @NotBlank @Size(max=120) String nombre,
  @NotBlank @Email @Size(max=160) String email,
  @Size(max=40) String telefono,
  @NotBlank @Size(max=80) String clave
) {}
