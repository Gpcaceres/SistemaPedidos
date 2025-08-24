import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  template: `
    <h2>Login</h2>
    <button (click)="login()">Ingresar</button>
    <div *ngIf="error" class="alert">{{ error }}</div>
  `
})
export class LoginComponent {
  error: string | null = null;

  constructor(private auth: AuthService) {}

  login() {
    this.auth.login().catch(() => (this.error = 'Error de autenticación'));
  }
}
