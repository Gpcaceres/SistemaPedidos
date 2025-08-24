import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-register',
  template: `
    <h2>Registro</h2>
    <form (ngSubmit)="register()">
      <label>Usuario</label>
      <input [(ngModel)]="username" name="username" />
      <label>Contraseña</label>
      <input [(ngModel)]="password" name="password" type="password" />
      <button type="submit">Registrar</button>
    </form>
    <div *ngIf="message" class="alert">{{ message }}</div>
  `
})
export class RegisterComponent {
  username = '';
  password = '';
  message: string | null = null;

  constructor(private auth: AuthService) {}

  register() {
    this.auth.register(this.username, this.password).subscribe(
      () => (this.message = 'Registro exitoso'),
      () => (this.message = 'Error de registro')
    );
  }
}
