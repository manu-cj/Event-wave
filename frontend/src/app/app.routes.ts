import { Routes } from '@angular/router';
import {AuthComponent} from './auth/auth.component';
import {Dashboard} from './dashboard/dashboard';

export const routes: Routes = [
  { path: 'login', component: AuthComponent },
  { path: 'dashboard', component: Dashboard }
];
