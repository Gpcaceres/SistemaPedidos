import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from './core/auth/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterOutlet],
  template: `
    <nav class="nav">
      <a routerLink="/orders">Pedidos</a>
      <a routerLink="/orders/new" *ngIf="isAuth">Nuevo</a>
      <span class="spacer"></span>
      <ng-container *ngIf="isAuth; else loginLink">
        <span>{{ username }}</span>
        <button (click)="logout()">Salir</button>
      </ng-container>
      <ng-template #loginLink>
        <a routerLink="/login">Ingresar</a>
      </ng-template>
    </nav>
    <router-outlet />
  `,
  styles: [`.nav{display:flex;gap:1rem;align-items:center;padding:10px;border-bottom:1px solid #ddd}.spacer{flex:1}`]
})
export class AppComponent {
  isAuth = false;
  username = '';
  constructor(private auth: AuthService){}
  ngOnInit(){ this.isAuth = this.auth.isAuthenticated(); this.username = this.auth.username(); }
  logout(){ this.auth.logout(); }
}
