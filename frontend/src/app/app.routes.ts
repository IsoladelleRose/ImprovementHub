import { Routes } from '@angular/router';
import { HowDoesItWork } from './how-does-it-work/how-does-it-work';
import { PartnerRegistration } from './partner-registration/partner-registration';
import { PartnerDetails } from './partner-details/partner-details';
import { IdeaRegistration } from './idea-registration/idea-registration';
import { RoleRegistration } from './role-registration/role-registration';
import { LoginComponent } from './login/login.component';
import { ProfileComponent } from './profile/profile.component';

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', loadComponent: () => import('./home/home').then(m => m.Home) },
  { path: 'how-does-it-work', component: HowDoesItWork },
  { path: 'partner-registration', component: PartnerRegistration },
  { path: 'partner-details', component: PartnerDetails },
  { path: 'idea-registration', component: IdeaRegistration },
  { path: 'role-registration', component: RoleRegistration },
  { path: 'login', component: LoginComponent },
  { path: 'profile', component: ProfileComponent },
  { path: '**', redirectTo: '/home' }
];
