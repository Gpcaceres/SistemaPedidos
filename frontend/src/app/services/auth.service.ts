import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'auth_token';
  private api = 'http://localhost:8080'; // gateway

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string) {
    const body = new URLSearchParams();
    body.set('grant_type', 'password');
    body.set('username', username);
    body.set('password', password);

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      Authorization: 'Basic ' + btoa('front-client:')
    });

    return this.http
      .post<any>(`${this.api}/oauth2/token`, body.toString(), { headers })
      .pipe(tap(res => localStorage.setItem(this.tokenKey, res.access_token)));
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
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
