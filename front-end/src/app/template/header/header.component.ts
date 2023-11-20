import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/seguranca/auth/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  constructor(private authService: AuthService,
              private router: Router) {}

  homeRoute() {
    this.router.navigate(['/home']);
  }

  controlUserRoute() {
    this.router.navigate(['/usuario/lista']);
  }

  logout() {
    this.authService.logout();
  }

}
