import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  template: `
    <h2>Login</h2>

    <form (ngSubmit)="login()" #f="ngForm" novalidate>
      <label for="username">Usuario</label>
      <input
        id="username"
        [(ngModel)]="username"
        name="username"
        required
        autocomplete="username"
      />

      <label for="password">Contraseña</label>
      <input
        id="password"
        [(ngModel)]="password"
        name="password"
        type="password"
        required
        autocomplete="current-password"
      />

      <button type="submit" [disabled]="loading || !f.valid">
        {{ loading ? 'Ingresando…' : 'Ingresar' }}
      </button>
    </form>

    <div *ngIf="error" class="alert">{{ error }}</div>
  `,
})
export class LoginComponent {
  username = '';
  password = '';
  error: string | null = null;
  loading = false;

  constructor(private auth: AuthService, private router: Router) {}

  login(): void {
    if (this.loading) return;
    this.error = null;
    this.loading = true;

    this.auth.login(this.username, this.password).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/mis-pedidos']);
      },
      error: (err) => {
        this.loading = false;
        // Manejo de red / backend
        if (err?.status === 0) {
          this.error = 'No se pudo conectar con el servidor';
        } else {
          this.error = err?.error?.message || 'Error de autenticación';
        }
      },
    });
  }
}
