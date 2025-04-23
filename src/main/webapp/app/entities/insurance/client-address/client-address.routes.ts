import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import ClientAddressResolve from './route/client-address-routing-resolve.service';

const clientAddressRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/client-address.component').then(m => m.ClientAddressComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/client-address-detail.component').then(m => m.ClientAddressDetailComponent),
    resolve: {
      clientAddress: ClientAddressResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/client-address-update.component').then(m => m.ClientAddressUpdateComponent),
    resolve: {
      clientAddress: ClientAddressResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/client-address-update.component').then(m => m.ClientAddressUpdateComponent),
    resolve: {
      clientAddress: ClientAddressResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default clientAddressRoute;
