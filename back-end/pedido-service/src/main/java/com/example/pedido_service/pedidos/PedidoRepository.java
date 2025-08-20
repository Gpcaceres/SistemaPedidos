package com.example.pedido_service.pedidos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
  List<Pedido> findByClienteId(UUID clienteId);
}
