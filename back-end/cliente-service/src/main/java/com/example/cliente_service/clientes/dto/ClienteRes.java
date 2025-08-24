package com.example.cliente_service.clientes.dto;
import com.example.cliente_service.clientes.Cliente;
import java.util.UUID;

public record ClienteRes(UUID id, String nombre, String email, String telefono) {
  public static ClienteRes of(Cliente c) {
    return new ClienteRes(c.getId(), c.getNombre(), c.getEmail(), c.getTelefono());
  }
}
