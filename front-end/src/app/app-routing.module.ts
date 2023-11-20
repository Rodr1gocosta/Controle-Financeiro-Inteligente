import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from './seguranca/auth/auth.guard.service';
import { LoginComponent } from './seguranca/login/login.component';
import { HomeComponent } from './home/home.component';

const routes: Routes = [
  {path: 'login', component: LoginComponent},

  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuardService] },

  { 
    path: 'usuario', 
    loadChildren: () => import('./seguranca/user/user.module').then(m => m.UserModule), 
    canActivate: [AuthGuardService] 
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AuthGuardService] 
})
export class AppRoutingModule { }
