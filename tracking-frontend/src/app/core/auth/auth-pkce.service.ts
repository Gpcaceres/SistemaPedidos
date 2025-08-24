import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthPkceService {
  constructor(private http: HttpClient, private router: Router){}

  private codeVerifierKey = 'pkce.verifier';
  private stateKey = 'pkce.state';

  private base64UrlEncode(buf: ArrayBuffer): string {
    let str = btoa(String.fromCharCode(...new Uint8Array(buf)));
    return str.replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
  }
  private async sha256(input: string): Promise<string> {
    const enc = new TextEncoder().encode(input);
    const digest = await crypto.subtle.digest('SHA-256', enc);
    return this.base64UrlEncode(digest);
  }

  async login(){
    const verifier = this.base64UrlEncode(crypto.getRandomValues(new Uint8Array(32)));
    const challenge = await this.sha256(verifier);
    const state = this.base64UrlEncode(crypto.getRandomValues(new Uint8Array(16)));
    localStorage.setItem(this.codeVerifierKey, verifier);
    localStorage.setItem(this.stateKey, state);

    const params = new URLSearchParams({
      response_type: 'code',
      client_id: environment.auth.clientId,
      redirect_uri: environment.auth.redirectUri,
      scope: 'openid profile clientes.read pedidos.read pedidos.write',
      code_challenge: challenge,
      code_challenge_method: 'S256',
      state
    });
    window.location.href = `${environment.endpoints.authAuthorize}?${params.toString()}`;
  }

  exchangeCode(code: string, state: string): Observable<any> {
    const expected = localStorage.getItem(this.stateKey) || '';
    const verifier = localStorage.getItem(this.codeVerifierKey) || '';
    localStorage.removeItem(this.stateKey);
    localStorage.removeItem(this.codeVerifierKey);
    if (state !== expected) throw new Error('PKCE state mismatch');

    const body = new HttpParams()
      .set('grant_type', 'authorization_code')
      .set('code', code)
      .set('redirect_uri', environment.auth.redirectUri)
      .set('client_id', environment.auth.clientId)
      .set('code_verifier', verifier);

    const headers = new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' });
    return this.http.post(environment.endpoints.authToken, body.toString(), { headers });
  }
}
