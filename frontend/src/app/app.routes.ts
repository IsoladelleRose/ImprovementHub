import { Routes } from '@angular/router';
import { Home } from './home/home';
import { HowDoesItWork } from './how-does-it-work/how-does-it-work';
import { PartnerRegistration } from './partner-registration/partner-registration';
import { PartnerDetails } from './partner-details/partner-details';
import { IdeaRegistration } from './idea-registration/idea-registration';
import { RoleRegistration } from './role-registration/role-registration';
import { LoginComponent } from './login/login.component';
import { ProfileComponent } from './roadmap/roadmap';
import { RagComponent } from './rag/rag.component';
import { IdeasStatus } from './ideas-status/ideas-status';

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: Home },
  { path: 'how-does-it-work', component: HowDoesItWork },
  { path: 'partner-registration', component: PartnerRegistration },
  { path: 'partner-details', component: PartnerDetails },
  { path: 'idea-registration', component: IdeaRegistration },
  { path: 'role-registration', component: RoleRegistration },
  { path: 'login', component: LoginComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'ideas-status', component: IdeasStatus },
  { path: 'roadmap', redirectTo: '/profile' },
  { path: 'rag', component: RagComponent },
  { path: 'dashboard', redirectTo: '/rag' },
  { path: '**', redirectTo: '/home' }
];
