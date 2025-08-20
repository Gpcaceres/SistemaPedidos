import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

export interface TokenResponse { access_token: string; refresh_token?: string; expires_in?: number; token_type?: string; id_token?: string; }

@Injectable({ providedIn: 'root' })
export class AuthService {
  private key = 'app.tokens';
  constructor(private router: Router){}

  saveToken(tok: TokenResponse){ localStorage.setItem(this.key, JSON.stringify(tok)); }
  token(): string | null { const raw = localStorage.getItem(this.key); if(!raw) return null; try{ return (JSON.parse(raw) as TokenResponse).access_token; }catch{ return null; } }
  idToken(): string | null { const raw = localStorage.getItem(this.key); if(!raw) return null; try{ return (JSON.parse(raw) as TokenResponse).id_token || null; }catch{ return null; } }
  clear(){ localStorage.removeItem(this.key); }
  isAuthenticated(): boolean { return !!this.token(); }
  username(): string {
    const id = this.idToken();
    if (!id) return '';
    try {
      const payload = JSON.parse(atob(id.split('.')[1]));
      return payload['sub'] || payload['preferred_username'] || '';
    } catch { return ''; }
  }
  roles(): string[] {
    const id = this.idToken();
    if (!id) return [];
    try {
      const payload = JSON.parse(atob(id.split('.')[1]));
      return payload['roles'] || [];
    } catch { return []; }
  }
  logout(){ this.clear(); this.router.navigate(['/login']); }
}
