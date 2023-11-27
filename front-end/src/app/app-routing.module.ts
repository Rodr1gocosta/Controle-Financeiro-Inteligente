import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from './seguranca/auth/auth.guard.service';
import { LoginComponent } from './seguranca/login/login.component';
import { HomeComponent } from './home/home.component';
import { FinanceiroComponent } from './components/financeiro/financeiro.component';
import { InvestimentoComponent } from './components/investimento/investimento.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

const routes: Routes = [
  {path: 'login', component: LoginComponent},

  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuardService] },

  { 
    path: 'usuario', 
    loadChildren: () => import('./seguranca/user/user.module').then(m => m.UserModule), 
    canActivate: [AuthGuardService] 
  },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuardService] },
  { path: 'financeiro', component: FinanceiroComponent, canActivate: [AuthGuardService] },
  { path: 'investimento', component: InvestimentoComponent, canActivate: [AuthGuardService] },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AuthGuardService] 
})
export class AppRoutingModule { }
