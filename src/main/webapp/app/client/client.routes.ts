import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ContractsComponent } from './contracts/contracts.component';
import { QuotesComponent } from './quotes/quotes.component';
import { ClaimsComponent } from './claims/claims.component';
import { AgencyComponent } from './agency/agency.component';

const routes: Routes = [
  {
    path: 'dashboard',
    component: DashboardComponent,
    title: 'Tableau de bord',
  },
  {
    path: 'contract',
    component: ContractsComponent,
    title: 'Contrats',
  },
  {
    path: 'quote',
    component: QuotesComponent,
    title: 'Devis',
  },
  {
    path: 'claim',
    component: ClaimsComponent,
    title: 'Réclamation',
  },
  {
    path: 'agency',
    component: AgencyComponent,
    title: 'Agences',
  },
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
  {
    path: '**',
    redirectTo: 'dashboard',
  },
];

export default routes;
