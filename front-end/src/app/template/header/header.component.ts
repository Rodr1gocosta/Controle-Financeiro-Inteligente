import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/seguranca/auth/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  isAdmin: boolean = false;

  timeRemaining!: number;
  private intervalId: any;

  constructor(private authService: AuthService,
    private router: Router) { }

  ngOnInit() {
    this.isAdmin = this.authService.isUserAdmin();

    this.intervalId = setInterval(() => {
      this.timeRemaining = this.authService.getTimeUntilTokenExpiration();

      if (this.timeRemaining === 0) {
        this.authService.logout();
        clearInterval(this.intervalId); 
      }
    }, 1000);
  }

  ngOnDestroy() {
    clearInterval(this.intervalId);
  }

  homeRoute() {
    this.router.navigate(['/home']);
  }

  controlUserRoute() {
    this.router.navigate(['/usuario/lista']);
  }

  controlCategoryDefaultRoute() {
    this.router.navigate(['/categoria-padrao']);
  }

  logout() {
    this.authService.logout();
  }

  formatTime(seconds: number): string {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const remainingSeconds = seconds % 60;
  
    // Limita o tempo a no máximo 24 horas
    const limitedHours = hours > 24 ? 24 : hours;
  
    const hoursDisplay = limitedHours > 0 ? limitedHours + 'h ' : '';
    const minutesDisplay = minutes > 0 ? minutes + 'min ' : '';
    const secondsDisplay = remainingSeconds > 0 ? remainingSeconds + 's' : '';
  
    return hoursDisplay + minutesDisplay + secondsDisplay;
  }

}
