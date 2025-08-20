package com.example.tracking_service.tracking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackingService {

  private final StringRedisTemplate redis;
  // Usa el ObjectMapper de Spring (ya viene con JavaTimeModule)
  private final ObjectMapper mapper;

  private String key(String pedidoId) { return "pedido:" + pedidoId; }

  public void upsert(TrackingEntry e) {
    try {
      if (e.getActualizadoEn() == null) e.setActualizadoEn(OffsetDateTime.now());
      String json = mapper.writeValueAsString(e);
      redis.opsForValue().set(key(e.getPedidoId().toString()), json);
    } catch (Exception ex) {
      log.error("Error guardando tracking en Redis", ex);
      throw new RuntimeException("No se pudo guardar tracking", ex);
    }
  }

  public TrackingEntry get(String pedidoId) {
    try {
      String json = redis.opsForValue().get(key(pedidoId));
      if (json == null) return null;
      return mapper.readValue(json, TrackingEntry.class);
    } catch (Exception ex) {
      log.error("Error leyendo tracking de Redis", ex);
      throw new RuntimeException("No se pudo leer tracking", ex);
    }
  }
}
