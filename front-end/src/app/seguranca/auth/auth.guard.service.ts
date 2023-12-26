import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { AuthService } from "./auth.service";

@Injectable({
    providedIn: 'root'
})
export class AuthGuardService {

    constructor(private authService: AuthService,
        private router: Router) {}

    canActivate(): boolean {
        if (this.authService.isAuthenticated()) {
            return true; // Permite o acesso à rota
        } else {
            this.router.navigate(['/login']); // Redireciona para a página de login
            return false; // Bloqueia o acesso à rota
        }
    }
}