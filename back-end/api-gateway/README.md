# API Gateway (Spring Cloud Gateway)

## Build & Run (Docker)
```bash
docker compose up --build
```
Gateway: http://localhost:8080

### Env
- AUTH_ISSUER_URI=http://host.docker.internal:9000
- CLIENTES_BASE_URL=http://host.docker.internal:8081
- PEDIDOS_BASE_URL=http://host.docker.internal:8082
- TRACKING_BASE_URL=http://host.docker.internal:8083
- CORS_ALLOWED_ORIGINS=http://localhost:4200,http://localhost:8080

## Test
- GET /api/tracking/{id} (público)
- /api/clientes/** (scopes clientes.read/clientes.write)
- /api/pedidos/**  (scopes pedidos.read/pedidos.write)
- /internal/**      (scope tracking.write)
