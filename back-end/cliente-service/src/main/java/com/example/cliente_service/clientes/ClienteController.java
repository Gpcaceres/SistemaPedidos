package com.example.cliente_service.clientes;

import com.example.cliente_service.clientes.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {
  private final ClienteRepository repo;
  private final PasswordEncoder encoder;

  @GetMapping
  public List<ClienteRes> listar() {
    return repo.findAll().stream().map(ClienteRes::of).toList();
  }

  @GetMapping("/{id}")
  public ClienteRes ver(@PathVariable UUID id) {
    return repo
        .findById(id)
        .map(ClienteRes::of)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ClienteRes crear(@Valid @RequestBody ClienteReq req) {
    var c = Cliente.builder()
        .nombre(req.nombre())
        .email(req.email())
        .telefono(req.telefono())
        .clave(encoder.encode(req.clave()))
        .tokenRecuperacion(UUID.randomUUID().toString())
        .build();
    return ClienteRes.of(repo.save(c));
  }

  @PutMapping("/{id}")
  public ClienteRes actualizar(@PathVariable UUID id, @Valid @RequestBody ClienteReq req) {
    var c =
        repo
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    c.setNombre(req.nombre());
    c.setEmail(req.email());
    c.setTelefono(req.telefono());
    c.setClave(encoder.encode(req.clave()));
    return ClienteRes.of(repo.save(c));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminar(@PathVariable UUID id) {
    if (!repo.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    repo.deleteById(id);
  }

  @PostMapping("/login")
  public ClienteRes login(@Valid @RequestBody LoginReq req) {
    var c = repo.findByEmail(req.email()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    if (!encoder.matches(req.clave(), c.getClave())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    return ClienteRes.of(c);
  }

  @PostMapping("/recuperar-clave")
  public ClienteRes recuperarClave(@Valid @RequestBody RecuperarClaveReq req) {
    var c = repo.findByEmail(req.email()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    if (!req.token().equals(c.getTokenRecuperacion())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    c.setClave(encoder.encode(req.nuevaClave()));
    c.setTokenRecuperacion(UUID.randomUUID().toString());
    return ClienteRes.of(repo.save(c));
  }
}
