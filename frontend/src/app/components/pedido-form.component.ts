import { Component } from '@angular/core';
import { PedidoService } from '../services/pedido.service';

@Component({
  selector: 'app-pedido-form',
  template: `
    <h2>Nuevo Pedido</h2>
    <form (ngSubmit)="crear()">
      <label>Cliente ID</label>
      <input [(ngModel)]="clienteId" name="clienteId" required />
      <label>Total</label>
      <input type="number" [(ngModel)]="total" name="total" required />
      <button type="submit">Crear</button>
    </form>
    <div *ngIf="mensaje">{{ mensaje }}</div>
  `
})
export class PedidoFormComponent {
  clienteId = '';
  total = 0;
  mensaje = '';

  constructor(private pedidos: PedidoService) {}

  crear() {
    this.pedidos.crear({ clienteId: this.clienteId, total: this.total }).subscribe(
      p => (this.mensaje = `Pedido ${p.id} creado`),
      () => (this.mensaje = 'Error al crear')
    );
  }
}
