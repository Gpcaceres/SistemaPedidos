import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { Order } from '../models/order.model';

@Injectable({ providedIn: 'root' })
export class OrdersService {
  private base = environment.apiBase + environment.endpoints.pedidos;
  constructor(private http: HttpClient) {}

  listByClient(clientId: string | number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.base}?clientId=${clientId}`);
  }
  getById(id: string | number): Observable<Order> {
    return this.http.get<Order>(`${this.base}/${id}`);
  }
  create(order: Order): Observable<Order> {
    return this.http.post<Order>(this.base, order);
  }
  update(id: string | number, order: Partial<Order>): Observable<Order> {
    return this.http.put<Order>(`${this.base}/${id}`, order);
  }
}
