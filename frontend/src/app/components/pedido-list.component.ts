import { Component } from '@angular/core';
import { Pedido, PedidoService } from '../services/pedido.service';

@Component({
  selector: 'app-pedido-list',
  template: `
    <h2>Mis pedidos</h2>
    <label>Cliente ID</label>
    <input [(ngModel)]="clienteId" />
    <button (click)="buscar()">Buscar</button>
    <ul>
      <li *ngFor="let p of pedidos">{{ p.id }} - {{ p.estado }} - {{ p.total }}</li>
    </ul>
  `
})
export class PedidoListComponent {
  clienteId = '';
  pedidos: Pedido[] = [];

  constructor(private service: PedidoService) {}

  buscar() {
    this.service.listarPorCliente(this.clienteId).subscribe(res => (this.pedidos = res));
  }
}
