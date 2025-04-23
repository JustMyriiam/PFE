import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'insuranceApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'client',
    data: { pageTitle: 'insuranceApp.insuranceClient.home.title' },
    loadChildren: () => import('./insurance/client/client.routes'),
  },
  {
    path: 'city',
    data: { pageTitle: 'insuranceApp.insuranceCity.home.title' },
    loadChildren: () => import('./insurance/city/city.routes'),
  },
  {
    path: 'governorate',
    data: { pageTitle: 'insuranceApp.insuranceGovernorate.home.title' },
    loadChildren: () => import('./insurance/governorate/governorate.routes'),
  },
  {
    path: 'client-address',
    data: { pageTitle: 'insuranceApp.insuranceClientAddress.home.title' },
    loadChildren: () => import('./insurance/client-address/client-address.routes'),
  },
  {
    path: 'claim',
    data: { pageTitle: 'insuranceApp.insuranceClaim.home.title' },
    loadChildren: () => import('./insurance/claim/claim.routes'),
  },
  {
    path: 'quote',
    data: { pageTitle: 'insuranceApp.insuranceQuote.home.title' },
    loadChildren: () => import('./insurance/quote/quote.routes'),
  },
  {
    path: 'contract',
    data: { pageTitle: 'insuranceApp.insuranceContract.home.title' },
    loadChildren: () => import('./insurance/contract/contract.routes'),
  },
  {
    path: 'agency',
    data: { pageTitle: 'insuranceApp.insuranceAgency.home.title' },
    loadChildren: () => import('./insurance/agency/agency.routes'),
  },
  {
    path: 'document',
    data: { pageTitle: 'insuranceApp.insuranceDocument.home.title' },
    loadChildren: () => import('./insurance/document/document.routes'),
  },
  {
    path: 'insurance-pack',
    data: { pageTitle: 'insuranceApp.insuranceInsurancePack.home.title' },
    loadChildren: () => import('./insurance/insurance-pack/insurance-pack.routes'),
  },
  {
    path: 'warranty',
    data: { pageTitle: 'insuranceApp.insuranceWarranty.home.title' },
    loadChildren: () => import('./insurance/warranty/warranty.routes'),
  },
  {
    path: 'vehicle',
    data: { pageTitle: 'insuranceApp.insuranceVehicle.home.title' },
    loadChildren: () => import('./insurance/vehicle/vehicle.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
