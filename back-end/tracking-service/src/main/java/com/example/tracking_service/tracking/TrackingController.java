package com.example.tracking_service.tracking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TrackingController {

  private final TrackingService service;

  @GetMapping("/api/tracking/{pedidoId}")
  public TrackingEntry get(@PathVariable String pedidoId) {
    var e = service.get(pedidoId);
    if (e == null) throw new ResourceNotFoundException();
    return e;
  }

  // Endpoint interno (se protegerá con JWT más adelante)
  @PostMapping("/internal/tracking/update")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void update(@Valid @RequestBody TrackingEntry entry) {
    service.upsert(entry);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  static class ResourceNotFoundException extends RuntimeException {}
}
