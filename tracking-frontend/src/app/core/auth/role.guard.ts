import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}
  canActivate(route: ActivatedRouteSnapshot): boolean | UrlTree {
    const allowed = (route.data?.['roles'] as string[]) || [];
    const roles = this.auth.roles();
    const ok = roles.some(r => allowed.includes(r) || allowed.includes(`ROLE_${r}`));
    return ok ? true : this.router.parseUrl('/orders');
  }
}
