import { Component } from '@angular/core';

interface Product {
  id: number;
  nombre: string;
  precio: number;
}

@Component({
  selector: 'app-products',
  template: `
    <h2>Productos</h2>
    <ul>
      <li *ngFor="let p of productos">{{ p.nombre }} - {{ p.precio | currency:'USD':'symbol' }}</li>
    </ul>
  `
})
export class ProductsComponent {
  productos: Product[] = [
    { id: 1, nombre: 'Producto A', precio: 10 },
    { id: 2, nombre: 'Producto B', precio: 20 },
    { id: 3, nombre: 'Producto C', precio: 30 }
  ];
}
