import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';

function base64UrlEncode(arrayBuffer: ArrayBuffer): string {
  const bytes = new Uint8Array(arrayBuffer);
  let binary = '';
  bytes.forEach(b => (binary += String.fromCharCode(b)));
  return btoa(binary).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'auth_token';
  private refreshKey = 'refresh_token';
  private verifierKey = 'pkce_code_verifier';
  private api = 'http://localhost:8080'; // gateway
  private redirectUri = 'http://localhost:4200/callback';

  constructor(private http: HttpClient, private router: Router) {}

  private generateCodeVerifier(): string {
    const array = new Uint8Array(64);
    window.crypto.getRandomValues(array);
    return Array.from(array, b => ('0' + b.toString(16)).slice(-2)).join('');
  }

  private async generateCodeChallenge(verifier: string): Promise<string> {
    const data = new TextEncoder().encode(verifier);
    const digest = await window.crypto.subtle.digest('SHA-256', data);
    return base64UrlEncode(digest);
  }

  async login(): Promise<void> {
    const verifier = this.generateCodeVerifier();
    sessionStorage.setItem(this.verifierKey, verifier);
    const challenge = await this.generateCodeChallenge(verifier);
    const authUrl = `${this.api}/oauth2/authorize?response_type=code&client_id=front-client&redirect_uri=${encodeURIComponent(this.redirectUri)}&scope=openid%20profile%20pedidos.read%20pedidos.write&code_challenge=${challenge}&code_challenge_method=S256`;
    window.location.href = authUrl;
  }

  handleAuthCallback() {
    const params = new URLSearchParams(window.location.search);
    const code = params.get('code');
    const verifier = sessionStorage.getItem(this.verifierKey) || '';
    const body = new URLSearchParams();
    body.set('grant_type', 'authorization_code');
    body.set('code', code || '');
    body.set('redirect_uri', this.redirectUri);
    body.set('client_id', 'front-client');
    body.set('code_verifier', verifier);
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded'
    });
    return this.http
      .post<any>(`${this.api}/oauth2/token`, body.toString(), { headers })
      .pipe(
        tap(res => {
          localStorage.setItem(this.tokenKey, res.access_token);
          if (res.refresh_token) {
            localStorage.setItem(this.refreshKey, res.refresh_token);
          }
        })
      );
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.refreshKey);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getRoles(): string[] {
    const token = this.getToken();
    if (!token) return [];
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload['roles'] || [];
    } catch {
      return [];
    }
  }

  hasRole(role: string): boolean {
    return this.getRoles().includes(role);
  }
}
