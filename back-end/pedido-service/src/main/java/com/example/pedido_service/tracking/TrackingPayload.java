package com.example.pedido_service.tracking;

import com.example.pedido_service.pedidos.PedidoEstado;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TrackingPayload {
  private UUID pedidoId;
  private UUID clienteId;
  private PedidoEstado estado;
  private BigDecimal total;
  private OffsetDateTime actualizadoEn;
}
