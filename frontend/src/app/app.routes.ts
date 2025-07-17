import { Routes } from '@angular/router';
import {AuthComponent} from './pages/auth/auth.component';
import {Dashboard} from './pages/dashboard/dashboard';
import {Register} from './pages/register/register';

export const routes: Routes = [
  { path: 'login', component: AuthComponent },
  { path: 'dashboard', component: Dashboard },
  { path: 'register', component: Register}
];
