import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./pages/login/login.page').then((m) => m.LoginPage),
  },
  {
    path: 'tabs',
    loadChildren: () =>
      import('./tabs/tabs.routes').then((m) => m.routes),
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/register/register.page').then( m => m.RegisterPage)
  },
  {
    path: 'donor-dashboard',
    loadComponent: () => import('./pages/donor-dashboard/donor-dashboard.page').then( m => m.DonorDashboardPage)
  },
  {
    path: 'patient-dashboard',
    loadComponent: () => import('./pages/patient-dashboard/patient-dashboard.page').then( m => m.PatientDashboardPage)
  },
  {
    path: 'hospital-dashboard',
    loadComponent: () => import('./pages/hospital-dashboard/hospital-dashboard.page').then( m => m.HospitalDashboardPage)
  },
  {
    path: 'bloodbank-dashboard',
    loadComponent: () => import('./pages/bloodbank-dashboard/bloodbank-dashboard.page').then( m => m.BloodbankDashboardPage)
  },
];