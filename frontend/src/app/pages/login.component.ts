import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  template: `
    <h2>Login</h2>
    <form (ngSubmit)="login()">
      <label>Usuario</label>
      <input [(ngModel)]="username" name="username" />
      <label>Contraseña</label>
      <input [(ngModel)]="password" name="password" type="password" />
      <button type="submit">Ingresar</button>
    </form>
    <div *ngIf="error" class="alert">{{ error }}</div>
  `
})
export class LoginComponent {
  username = '';
  password = '';
  error: string | null = null;

  constructor(private auth: AuthService, private router: Router) {}

  login() {
    this.auth.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/mis-pedidos']),
      error: err => {
        this.error = err.error?.message || 'Error de autenticación';
      }
    });
  }
}
