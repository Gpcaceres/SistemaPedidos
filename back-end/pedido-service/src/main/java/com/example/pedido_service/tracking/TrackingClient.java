package com.example.pedido_service.tracking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class TrackingClient {

  private final RestClient.Builder builder;

  @Value("${tracking.base-url:http://host.docker.internal:8083}")
  private String baseUrl;

  @Value("${auth.base-url:http://host.docker.internal:9000}")
  private String authBase;

  @Value("${auth.client-id:pedido-service}")
  private String clientId;

  @Value("${auth.client-secret:pedido-secret}")
  private String clientSecret;

  private String cachedToken;
  private Instant tokenExp;

  private String getToken() {
    if (cachedToken != null && tokenExp != null && tokenExp.isAfter(Instant.now().plusSeconds(30))) {
      return cachedToken;
    }
    String body = "grant_type=client_credentials&client_id=" + clientId +
                  "&client_secret=" + clientSecret + "&scope=tracking.write";
    var resp = builder.baseUrl(authBase).build()
      .post()
      .uri("/oauth2/token")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .body(body)
      .retrieve()
      .body(Map.class);
    cachedToken = (String) resp.get("access_token");
    Number expiresIn = (Number) resp.get("expires_in");
    tokenExp = Instant.now().plusSeconds(expiresIn != null ? expiresIn.longValue() : 3000);
    return cachedToken;
  }

  public void upsert(TrackingPayload payload) {
    try {
      String token = getToken();
      builder.baseUrl(baseUrl).build()
        .post()
        .uri("/internal/tracking/update")
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer " + token)
        .body(payload)
        .retrieve()
        .toBodilessEntity();
    } catch (Exception ex) {
      log.warn("No se pudo actualizar tracking (se intentará en próxima operación): {}", ex.toString());
    }
  }
}
