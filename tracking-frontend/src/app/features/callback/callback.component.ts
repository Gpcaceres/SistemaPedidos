import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthPkceService } from '../../core/auth/auth-pkce.service';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  standalone: true,
  selector: 'app-callback',
  imports: [CommonModule],
  template: `<p>Procesando inicio de sesión...</p>`
})
export class CallbackComponent {
  private router = inject(Router);
  private pkce = inject(AuthPkceService);
  private auth = inject(AuthService);

  ngOnInit(){
    const url = new URL(window.location.href);
    const code = url.searchParams.get('code');
    const state = url.searchParams.get('state') || '';
    if (!code) { this.router.navigate(['/login']); return; }

    this.pkce.exchangeCode(code, state).subscribe({
      next: (tok: any) => { this.auth.saveToken(tok); this.router.navigate(['/']); },
      error: (_err: any) => this.router.navigate(['/login'])
    });
  }
}
