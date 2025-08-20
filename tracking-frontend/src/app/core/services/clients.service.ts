import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

export interface Client { id: number|string; name: string; email?: string; }

@Injectable({ providedIn: 'root' })
export class ClientsService {
  private base = environment.apiBase + environment.endpoints.clientes;
  constructor(private http: HttpClient) {}
  list(): Observable<Client[]> { return this.http.get<Client[]>(this.base); }
}
