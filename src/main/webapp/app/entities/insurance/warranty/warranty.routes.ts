import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import WarrantyResolve from './route/warranty-routing-resolve.service';

const warrantyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/warranty.component').then(m => m.WarrantyComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/warranty-detail.component').then(m => m.WarrantyDetailComponent),
    resolve: {
      warranty: WarrantyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/warranty-update.component').then(m => m.WarrantyUpdateComponent),
    resolve: {
      warranty: WarrantyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/warranty-update.component').then(m => m.WarrantyUpdateComponent),
    resolve: {
      warranty: WarrantyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default warrantyRoute;
