# Cliente Service

## Password Recovery

Each client has a recovery token stored in the `tokenRecuperacion` field. The token is automatically generated when a client account is created and returned in the service responses. Clients should store this token securely.

### Resetting the Password

1. Send a request to `POST /api/clientes/recuperar-clave` with the following body:
   ```json
   {
     "email": "usuario@example.com",
     "token": "<tokenRecuperacion>",
     "nuevaClave": "nuevaPassword"
   }
   ```
2. The service validates the token before updating the password.
3. A new recovery token is generated and included in the response.

If the token does not match, the service returns `401 Unauthorized`.

