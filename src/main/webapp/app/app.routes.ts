import { Routes } from '@angular/router';

import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';

const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./home/home.component'),
    title: 'home.title',
  },
  {
    path: '',
    loadComponent: () => import('./layouts/navbar/navbar.component'),
    outlet: 'navbar',
  },
  {
    path: '',
    loadComponent: () => import('./layouts/sidebar/sidebar.component'),
    outlet: 'sidebar',
  },
  {
    path: '',
    outlet: 'admin-sidebar',
    loadComponent: () => import('./admin/admin-sidebar/admin-sidebar.component').then(m => m.AdminSidebarComponent),
  },
  {
    path: '',
    outlet: 'user-sidebar',
    loadComponent: () => import('./user/user-sidebar/user-sidebar.component').then(m => m.UserSidebarComponent),
  },
  {
    path: 'agency',
    loadComponent: () => import('./user/agency/agency.component').then(m => m.AgencyComponent),
  },
  {
    path: 'admin',
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./admin/admin.routes'),
  },
  {
    path: 'user',
    data: {
      authorities: [Authority.USER],
    },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./user/user.routes'),
  },
  {
    path: '',
    loadChildren: () => import(`./entities/entity.routes`),
  },
  ...errorRoute,
];

export default routes;
