package com.example.pedido_service.pedidos.dto;

import com.example.pedido_service.pedidos.Pedido;
import com.example.pedido_service.pedidos.PedidoEstado;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @AllArgsConstructor
public class PedidoRes {
  private UUID id;
  private UUID clienteId;
  private PedidoEstado estado;
  private BigDecimal total;
  private OffsetDateTime creadoEn;
  private OffsetDateTime actualizadoEn;

  public static PedidoRes of(Pedido p) {
    return new PedidoRes(
      p.getId(), p.getClienteId(), p.getEstado(), p.getTotal(), p.getCreadoEn(), p.getActualizadoEn()
    );
  }
}
