import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Pedido {
  id: string;
  clienteId: string;
  total: number;
  estado: string;
  creadoEn: string;
  actualizadoEn: string;
}

@Injectable({ providedIn: 'root' })
export class PedidoService {
  private api = 'http://localhost:8080/api/pedidos';

  constructor(private http: HttpClient) {}

  crear(pedido: { clienteId: string; total: number }): Observable<Pedido> {
    return this.http.post<Pedido>(this.api, pedido);
  }

  listarPorCliente(clienteId: string): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(`${this.api}/cliente/${clienteId}`);
  }

  ver(id: string): Observable<Pedido> {
    return this.http.get<Pedido>(`${this.api}/${id}`);
  }
}
