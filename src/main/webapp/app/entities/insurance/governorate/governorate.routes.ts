import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import GovernorateResolve from './route/governorate-routing-resolve.service';

const governorateRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/governorate.component').then(m => m.GovernorateComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/governorate-detail.component').then(m => m.GovernorateDetailComponent),
    resolve: {
      governorate: GovernorateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/governorate-update.component').then(m => m.GovernorateUpdateComponent),
    resolve: {
      governorate: GovernorateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/governorate-update.component').then(m => m.GovernorateUpdateComponent),
    resolve: {
      governorate: GovernorateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default governorateRoute;
