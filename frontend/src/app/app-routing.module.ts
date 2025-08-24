import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login.component';
import { CallbackComponent } from './pages/callback.component';
import { PedidoFormComponent } from './components/pedido-form.component';
import { PedidoListComponent } from './components/pedido-list.component';
import { TrackingComponent } from './components/tracking.component';
import { AuthGuard } from './guards/auth.guard';
import { RoleGuard } from './guards/role.guard';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'callback', component: CallbackComponent },
  {
    path: 'nuevo',
    component: PedidoFormComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'CLIENTE' }
  },
  {
    path: 'mis-pedidos',
    component: PedidoListComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'CLIENTE' }
  },
  {
    path: 'tracking',
    component: TrackingComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'CLIENTE' }
  },
  { path: '', redirectTo: 'mis-pedidos', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
