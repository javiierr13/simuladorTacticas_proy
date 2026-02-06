import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegistroComponent } from './components/registro/registro.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { JugadoresComponent } from './components/jugadores/jugadores.component';
import { AlineacionesComponent } from './components/alineaciones/alineaciones.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'jugadores', component: JugadoresComponent },
  { path: 'alineaciones', component: AlineacionesComponent }
];
