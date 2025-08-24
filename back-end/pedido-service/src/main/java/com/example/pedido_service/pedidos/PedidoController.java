package com.example.pedido_service.pedidos;

import com.example.pedido_service.pedidos.dto.*;
import com.example.pedido_service.tracking.TrackingClient;
import com.example.pedido_service.tracking.TrackingPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

  private final PedidoRepository repo;
  private final TrackingClient trackingClient;

  @GetMapping
  public List<PedidoRes> listar() {
    return repo.findAll().stream().map(PedidoRes::of).toList();
  }

  @GetMapping("/{id}")
  public PedidoRes ver(@PathVariable UUID id) {
    return repo.findById(id)
        .map(PedidoRes::of)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @GetMapping("/cliente/{clienteId}")
  public List<PedidoRes> listarPorCliente(@PathVariable UUID clienteId) {
    return repo.findByClienteId(clienteId).stream().map(PedidoRes::of).toList();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PedidoRes crear(@Valid @RequestBody PedidoCreateReq req) {
    var p = Pedido.builder()
        .clienteId(req.getClienteId())
        .total(req.getTotal())
        .estado(PedidoEstado.CREADO)
        .build();
    var saved = repo.save(p);
    trackingClient.upsert(TrackingPayload.builder()
        .pedidoId(saved.getId())
        .clienteId(saved.getClienteId())
        .estado(saved.getEstado())
        .total(saved.getTotal())
        .actualizadoEn(saved.getActualizadoEn())
        .build());
    return PedidoRes.of(saved);
  }

  @PutMapping("/{id}")
  public PedidoRes actualizar(@PathVariable UUID id, @Valid @RequestBody PedidoUpdateReq req) {
    var p = repo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    if (req.getTotal() != null) p.setTotal(req.getTotal());
    if (req.getEstado() != null) p.setEstado(req.getEstado());
    var updated = repo.save(p);
    trackingClient.upsert(TrackingPayload.builder()
        .pedidoId(updated.getId())
        .clienteId(updated.getClienteId())
        .estado(updated.getEstado())
        .total(updated.getTotal())
        .actualizadoEn(updated.getActualizadoEn())
        .build());
    return PedidoRes.of(updated);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminar(@PathVariable UUID id) {
    if (!repo.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    repo.deleteById(id);
  }
}
