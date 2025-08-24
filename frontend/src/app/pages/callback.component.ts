import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-callback',
  template: `<p>Procesando autenticación...</p>`
})
export class CallbackComponent implements OnInit {
  constructor(private auth: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.auth.handleAuthCallback().subscribe({
      next: () => this.router.navigate(['/mis-pedidos']),
      error: () => this.router.navigate(['/login'])
    });
  }
}
