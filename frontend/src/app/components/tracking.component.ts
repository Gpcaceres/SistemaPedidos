import { Component } from '@angular/core';
import { PedidoService, Pedido } from '../services/pedido.service';
import { TrackingService, TrackingEntry } from '../services/tracking.service';

@Component({
  selector: 'app-tracking',
  template: `
    <h2>Tracking</h2>
    <label>ID Pedido</label>
    <input [(ngModel)]="pedidoId" />
    <button (click)="consultar()">Consultar</button>
    <div *ngIf="pedido">
      <h3>MySQL</h3>
      <pre>{{ pedido | json }}</pre>
    </div>
    <div *ngIf="tracking">
      <h3>Redis</h3>
      <pre>{{ tracking | json }}</pre>
    </div>
    <div *ngIf="pedido && tracking && discrepancia" class="alert">Datos desactualizados</div>
  `
})
export class TrackingComponent {
  pedidoId = '';
  pedido: Pedido | null = null;
  tracking: TrackingEntry | null = null;
  discrepancia = false;

  constructor(
    private pedidos: PedidoService,
    private trackingService: TrackingService
  ) {}

  consultar() {
    this.pedido = null;
    this.tracking = null;
    this.discrepancia = false;

    this.pedidos.ver(this.pedidoId).subscribe(p => {
      this.pedido = p;
      this.compare();
    });

    this.trackingService.obtener(this.pedidoId).subscribe(t => {
      this.tracking = t;
      this.compare();
    });
  }

  private compare() {
    if (this.pedido && this.tracking) {
      this.discrepancia =
        this.pedido.estado !== this.tracking.estado ||
        this.pedido.total !== this.tracking.total;
    }
  }
}
