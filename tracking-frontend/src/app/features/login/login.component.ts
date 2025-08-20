import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthPkceService } from '../../core/auth/auth-pkce.service';

@Component({
  standalone: true,
  selector: 'app-login',
  imports: [CommonModule],
  template: `
    <h2>Ingreso</h2>
    <button (click)="login()">Entrar con OAuth2 (PKCE)</button>
  `
})
export class LoginComponent {
  private pkce = inject(AuthPkceService);
  login(){ this.pkce.login(); }
}
