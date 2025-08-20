import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { OrdersService } from '../../core/services/orders.service';
import { AuthService } from '../../core/auth/auth.service';
import { Order } from '../../core/models/order.model';

@Component({
  standalone: true,
  selector: 'app-orders-list',
  imports: [CommonModule, RouterLink],
  template: `
    <h2>Mis pedidos</h2>
    <div *ngIf="orders.length === 0">No hay pedidos</div>
    <ul>
      <li *ngFor="let o of orders">
        <a [routerLink]="['/orders', o.id]">#{{o.id}} — {{o.product}} — {{o.status || 'N/A'}}</a>
      </li>
    </ul>
  `
})
export class OrdersListComponent {
  private ordersSvc = inject(OrdersService);
  private auth = inject(AuthService);
  orders: Order[] = [];

  ngOnInit(){
    const clientId = this.auth.username();
    if (!clientId) return;
    this.ordersSvc.listByClient(clientId).subscribe((res: Order[]) => this.orders = res);
  }
}
