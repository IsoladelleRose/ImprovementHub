import { Routes } from '@angular/router';
import { HowDoesItWork } from './how-does-it-work/how-does-it-work';
import { PartnerRegistration } from './partner-registration/partner-registration';

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', loadComponent: () => import('./home/home').then(m => m.Home) },
  { path: 'how-does-it-work', component: HowDoesItWork },
  { path: 'partner-registration', component: PartnerRegistration },
  { path: '**', redirectTo: '/home' }
];
