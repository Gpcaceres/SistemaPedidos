package com.example.cliente_service.clientes;

import com.example.cliente_service.clientes.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {
  private final ClienteRepository repo;

  @GetMapping
  public List<ClienteRes> listar() {
    return repo.findAll().stream().map(ClienteRes::of).toList();
  }

  @GetMapping("/{id}")
  public ClienteRes ver(@PathVariable UUID id) {
    return repo.findById(id).map(ClienteRes::of).orElseThrow();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ClienteRes crear(@Valid @RequestBody ClienteReq req) {
    var c = Cliente.builder()
        .nombre(req.nombre())
        .email(req.email())
        .telefono(req.telefono())
        .build();
    return ClienteRes.of(repo.save(c));
  }

  @PutMapping("/{id}")
  public ClienteRes actualizar(@PathVariable UUID id, @Valid @RequestBody ClienteReq req) {
    var c = repo.findById(id).orElseThrow();
    c.setNombre(req.nombre());
    c.setEmail(req.email());
    c.setTelefono(req.telefono());
    return ClienteRes.of(repo.save(c));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminar(@PathVariable UUID id) {
    repo.deleteById(id);
  }
}
