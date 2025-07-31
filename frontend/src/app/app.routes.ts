import { Routes } from '@angular/router';
import {AuthComponent} from './pages/auth/auth.component';
import {Dashboard} from './pages/dashboard/dashboard';
import {Register} from './pages/register/register';
import {Events} from './pages/events/events';
import {EventDetails} from './pages/event-details/eventDetails';
import {Reservations} from './pages/reservations/reservations';
import {App} from './app';

export const routes: Routes = [
  { path: 'login', component: AuthComponent },
  { path: 'dashboard', component: Dashboard },
  { path: 'register', component: Register},
  { path: 'events', component: Events},
  { path: 'event/:id', component: EventDetails },
  { path: 'reservations', component: Reservations}
];
