export const environment = {
  production: false,
  apiBase: 'http://localhost:8080',
  endpoints: {
    authAuthorize: 'http://localhost:9000/oauth2/authorize',
    authToken:     'http://localhost:9000/oauth2/token',
    clientes: '/api/clientes',
    pedidos:  '/api/pedidos',
    tracking: '/api/tracking',
  },
  auth: {
    flow: 'pkce',
    clientId: 'front-client',
    redirectUri: 'http://localhost:4200/callback',
    scopes: ['openid','profile','clientes.read','pedidos.read','pedidos.write']
  }
};