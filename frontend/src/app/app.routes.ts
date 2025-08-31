import { Routes } from '@angular/router';
import { HowDoesItWork } from './how-does-it-work/how-does-it-work';

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', loadComponent: () => import('./home/home').then(m => m.Home) },
  { path: 'how-does-it-work', component: HowDoesItWork },
  { path: '**', redirectTo: '/home' }
];
