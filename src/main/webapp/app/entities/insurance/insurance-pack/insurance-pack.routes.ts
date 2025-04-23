import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import InsurancePackResolve from './route/insurance-pack-routing-resolve.service';

const insurancePackRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/insurance-pack.component').then(m => m.InsurancePackComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/insurance-pack-detail.component').then(m => m.InsurancePackDetailComponent),
    resolve: {
      insurancePack: InsurancePackResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/insurance-pack-update.component').then(m => m.InsurancePackUpdateComponent),
    resolve: {
      insurancePack: InsurancePackResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/insurance-pack-update.component').then(m => m.InsurancePackUpdateComponent),
    resolve: {
      insurancePack: InsurancePackResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default insurancePackRoute;
