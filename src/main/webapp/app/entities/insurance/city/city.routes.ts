import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import CityResolve from './route/city-routing-resolve.service';

const cityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/city.component').then(m => m.CityComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/city-detail.component').then(m => m.CityDetailComponent),
    resolve: {
      city: CityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/city-update.component').then(m => m.CityUpdateComponent),
    resolve: {
      city: CityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/city-update.component').then(m => m.CityUpdateComponent),
    resolve: {
      city: CityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cityRoute;
