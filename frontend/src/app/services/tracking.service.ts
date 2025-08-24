import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface TrackingEntry {
  pedidoId: string;
  clienteId: string;
  estado: string;
  total: number;
  actualizadoEn: string;
}

@Injectable({ providedIn: 'root' })
export class TrackingService {
  private api = 'http://localhost:8080/api/tracking';

  constructor(private http: HttpClient) {}

  obtener(pedidoId: string): Observable<TrackingEntry> {
    return this.http.get<TrackingEntry>(`${this.api}/${pedidoId}`);
  }
}
