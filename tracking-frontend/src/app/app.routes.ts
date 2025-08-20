import { Routes } from '@angular/router';
import { LoginComponent } from './features/login/login.component';
import { OrdersListComponent } from './features/orders/orders-list.component';
import { OrderCreateComponent } from './features/orders/order-create.component';
import { OrderDetailComponent } from './features/orders/order-detail.component';
import { AuthGuard } from './core/auth/auth.guard';
import { RoleGuard } from './core/auth/role.guard';
import { CallbackComponent } from './features/callback/callback.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'callback', component: CallbackComponent },
  {
    path: 'orders',
    canActivate: [AuthGuard],
    children: [
      { path: '', component: OrdersListComponent },
      { path: 'new', component: OrderCreateComponent, canActivate: [RoleGuard], data: { roles: ['ROLE_ADMIN','ROLE_CLIENTE'] } },
      { path: ':id', component: OrderDetailComponent }
    ]
  },
  { path: '', redirectTo: 'orders', pathMatch: 'full' },
  { path: '**', redirectTo: 'orders' }
];
