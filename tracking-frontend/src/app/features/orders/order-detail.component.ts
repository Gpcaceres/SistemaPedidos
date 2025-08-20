import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { OrdersService } from '../../core/services/orders.service';
import { TrackingService } from '../../core/services/tracking.service';
import { Order } from '../../core/models/order.model';
import { TrackingState } from '../../core/models/tracking.model';
import { StatusBadgeComponent } from '../../shared/components/status-badge.component';

@Component({
  standalone: true,
  selector: 'app-order-detail',
  imports: [CommonModule, StatusBadgeComponent],
  template: `
    <h2>Pedido #{{order?.id}}</h2>
    <div *ngIf="order; else loading">
      <p><b>Producto:</b> {{order!.product}}</p>
      <p><b>Dirección:</b> {{order!.address}}</p>
      <p><b>Status (MySQL):</b> {{order!.status}} <small>({{ order!.updatedAt || 's/fecha' }})</small></p>

      <ng-container *ngIf="tracking">
        <p><b>Estado (Redis):</b> {{tracking!.status}} <small>({{ tracking!.updatedAt || 's/fecha' }})</small></p>
        <status-badge [text]="staleText" [warn]="stale" [ok]="!stale && !!tracking"></status-badge>
      </ng-container>

      <p *ngIf="!tracking">No hay datos de tracking (Redis).</p>
    </div>
    <ng-template #loading>Cargando...</ng-template>
  `
})
export class OrderDetailComponent {
  private route = inject(ActivatedRoute);
  private orders = inject(OrdersService);
  private trackingSvc = inject(TrackingService);

  order?: Order; tracking?: TrackingState; stale = false; staleText = '';

  ngOnInit(){
    const id = this.route.snapshot.paramMap.get('id')!;
    this.orders.getById(id).subscribe((o: Order) => { this.order = o; this.fetchTracking(id); });
  }

  fetchTracking(id: string){
    this.trackingSvc.getState(id).subscribe({
      next: (t: TrackingState) => {
        this.tracking = t;
        this.checkConsistency();
      },
      error: (_err: any) => { this.tracking = undefined; this.stale = true; this.staleText = 'Sin tracking (posible inconsistencia)'; }
    });
  }

  private checkConsistency(){
    if (!this.order || !this.tracking) { this.stale = true; this.staleText = 'Datos incompletos'; return; }
    const tOrder = this.order.updatedAt ? Date.parse(this.order.updatedAt) : 0;
    const tRedis = this.tracking.updatedAt ? Date.parse(this.tracking.updatedAt) : 0;
    this.stale = tRedis + 5000 < tOrder;
    this.staleText = this.stale ? 'Redis desactualizado respecto a MySQL' : 'Consistente';
  }
}
