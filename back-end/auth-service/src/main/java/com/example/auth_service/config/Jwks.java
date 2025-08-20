package com.example.auth_service.config;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;

public final class Jwks {
  private Jwks() {}
  public static RSAKey generateRsa() {
    try {
      return new RSAKeyGenerator(2048).keyID("auth-key").generate();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
