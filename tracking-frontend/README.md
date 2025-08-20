# Frontend — Sistema de Seguimiento de Pedidos (Angular 18 standalone)

## Requisitos
- Node 20+
- Angular CLI 18+ (`npm i -g @angular/cli`)

## Arranque
```bash
npm install
npm start
```

## Config
- `src/environments/environment.ts` ya apunta a:
  - Gateway: `http://localhost:8080`
  - Auth (PKCE): `http://localhost:9000/oauth2/authorize` y `/oauth2/token`
  - Client ID: `front-client` (según tu auth-service)
  - Redirect URI: `http://localhost:4200/callback`

## Rutas principales
- `/login` → botón que inicia flujo **Authorization Code + PKCE**
- `/callback` → canjea el `code` por tokens y guarda en `localStorage`
- `/orders` (protegida) → lista pedidos por cliente (usa `username()` del JWT)
- `/orders/new` (protegida + roles) → crea pedido
- `/orders/:id` → comparación MySQL (pedido-service) vs Redis (tracking-service)

## Notas
- Si cambias los claims de roles o el `sub` en tu JWT, ajusta `AuthService`.
- Si sirves el front en otro host, actualiza `redirectUri` y CORS en el auth-server.
