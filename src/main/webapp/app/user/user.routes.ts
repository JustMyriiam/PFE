import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ContractsComponent } from './contracts/contracts.component';
import { QuotesComponent } from './quotes/quotes.component';
import { AgencyComponent } from './agency/agency.component';
import { Stepper1Component } from './stepper-1/stepper-1.component';
import { ClaimsComponent } from './claims/claims.component';
import { AddClaimComponent } from './add-claim/add-claim.component';
import { AddQuoteComponent } from './add-quote/add-quote.component';

const routes: Routes = [
  {
    path: 'dashboard',
    component: DashboardComponent,
    title: 'Tableau de bord',
  },
  {
    path: 'contracts',
    component: ContractsComponent,
    title: 'Contrats',
  },
  {
    path: 'quotes',
    component: QuotesComponent,
    title: 'Devis',
  },
  {
    path: 'claims',
    component: ClaimsComponent,
    title: 'Réclamation',
  },
  {
    path: 'agency',
    component: AgencyComponent,
    title: 'Agences',
  },
  {
    path: 'addQuote',
    component: AddQuoteComponent,
    title: 'add Quote',
  },
  {
    path: 'addClaim',
    component: AddClaimComponent,
    title: 'add claim',
  },
  {
    path: 'addContract',
    component: Stepper1Component,
    title: 'add Contract',
  },
  // {
  //   path: '',
  //   redirectTo: 'dashboard',
  //   pathMatch: 'full',
  // },
  // {
  //   path: '**',
  //   redirectTo: 'dashboard',
  // },
];

export default routes;
