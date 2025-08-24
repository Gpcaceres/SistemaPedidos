import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'auth_token';
  private refreshKey = 'refresh_token';
  private api = 'http://localhost:8080'; // gateway
  private authApi = 'http://localhost:9000/auth';

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string) {
    return this.http
      .post<any>(`${this.authApi}/login`, { username, password })
      .pipe(
        tap(res => {
          localStorage.setItem(this.tokenKey, res.accessToken);
          if (res.refreshToken) {
            localStorage.setItem(this.refreshKey, res.refreshToken);
          }
        })
      );
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.refreshKey);
    this.router.navigate(['/login']);
  }

  register(username: string, password: string) {
    return this.http.post(`${this.authApi}/register`, { username, password, role: 'CLIENTE' });
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
