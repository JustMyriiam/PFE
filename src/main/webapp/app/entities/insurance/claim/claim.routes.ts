import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import ClaimResolve from './route/claim-routing-resolve.service';

const claimRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/claim.component').then(m => m.ClaimComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/claim-detail.component').then(m => m.ClaimDetailComponent),
    resolve: {
      claim: ClaimResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/claim-update.component').then(m => m.ClaimUpdateComponent),
    resolve: {
      claim: ClaimResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/claim-update.component').then(m => m.ClaimUpdateComponent),
    resolve: {
      claim: ClaimResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default claimRoute;
