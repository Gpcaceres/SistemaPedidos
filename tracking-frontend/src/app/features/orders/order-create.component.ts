import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { OrdersService } from '../../core/services/orders.service';
import { ClientsService, Client } from '../../core/services/clients.service';

@Component({
  standalone: true,
  selector: 'app-order-create',
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <h2>Nuevo pedido</h2>
    <form [formGroup]="form" (ngSubmit)="submit()" class="card">
      <label>Producto <input formControlName="product" /></label>
      <label>Cliente
        <select formControlName="clientId">
          <option *ngFor="let c of clients" [value]="c.id">{{c.name}}</option>
        </select>
      </label>
      <label>Dirección <input formControlName="address" /></label>
      <button [disabled]="form.invalid || loading">Crear</button>
      <p class="error" *ngIf="error">{{ error }}</p>
    </form>
  `,
  styles: [`.card{display:flex;flex-direction:column;gap:.5rem;max-width:420px}`]
})
export class OrderCreateComponent {
  private fb = inject(FormBuilder);
  private ordersSvc = inject(OrdersService);
  private clientsSvc = inject(ClientsService);
  private router = inject(Router);

  loading=false; error=''; clients: Client[]=[];
  form = this.fb.group({ product: ['', Validators.required], clientId: ['', Validators.required], address: ['', Validators.required] });

  ngOnInit(){ this.clientsSvc.list().subscribe((cs: Client[]) => this.clients = cs); }

  submit(){
    this.loading=true; this.error='';
    this.ordersSvc.create(this.form.value as any).subscribe({
      next: (o: any) => this.router.navigate(['/orders', o.id]),
      error: (_err: any) => { this.error='Error creando pedido'; this.loading=false; }
    });
  }
}
