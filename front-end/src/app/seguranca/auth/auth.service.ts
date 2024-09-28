import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { Observable, map } from "rxjs";
import { environment } from "src/environments/environments";
import { jwtDecode } from "jwt-decode";
import { MessageOperationService } from "src/app/shared/util/message-operation/message-operation.service";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    baseUrl: String = environment.baseUrl;

    private tokenExpiration?: Date;
    private tokenKey = 'token';
    private decodedToken: any;

    constructor(private http: HttpClient,
        private router: Router,
        private message: MessageOperationService
    ) {
        this.initializeTokenExpiration();
    }

    login(username: string, password: string): Observable<any> {
        const url = this.baseUrl + "/api/security/authenticate";
        this.clearToken();

        return this.http.post<any>(url, { username, password }).pipe(
            map((response: any) => {
                this.saveToken(response.token);
            })
        );
    }

    logout() {
        this.clearToken();
        this.router.navigate(['/login']);
        this.message.message('Sessão finalizada', 'warn');
        window.location.reload();
    }

    isAuthenticated(): boolean {
        const token = localStorage.getItem('token');

        if (token && !this.isTokenExpired(token)) {
            return true;
        }

        return false;
    }

    isUserAdmin(): boolean {
        const token = localStorage.getItem(this.tokenKey);
    
        if (!token) {
            return false;
        }
    
        try {
            this.decodedToken = jwtDecode<{ roles: string | string[] }>(token);
    
            const roles = this.decodedToken.roles;
    
            if (Array.isArray(roles)) {
                return roles.includes("ROLE_ADMIN");
            }
    
            return roles === "ROLE_ADMIN";
        } catch (error) {
            console.error("Erro ao decodificar o token:", error);
            return false;
        }
    }

    getTimeUntilTokenExpiration(): number {
        if (this.tokenExpiration) {
            const currentTime = new Date().getTime();
            const expirationTime = this.tokenExpiration.getTime();

            const timeRemainingInSeconds = Math.floor((expirationTime - currentTime) / 1000);

            return Math.max(0, timeRemainingInSeconds);
        }

        return 0;
    }

    private saveToken(token: string) {
        this.decodedToken = jwtDecode(token);
        this.tokenExpiration = new Date(this.decodedToken.exp * 1000);

        localStorage.setItem('token', token);
    }

    private isTokenExpired(token: string): boolean {
        try {
            const decodedToken: any = jwtDecode(token);

            if (decodedToken && decodedToken.iat && decodedToken.exp) {
                const currentDateInSeconds = Math.floor(new Date().getTime() / 1000);

                return currentDateInSeconds < decodedToken.iat || currentDateInSeconds >= decodedToken.exp;
            }

            return true;
        } catch (error) {
            return true;
        }
    }

    private initializeTokenExpiration() {
        const storedToken = localStorage.getItem(this.tokenKey);
        if (storedToken) {
            this.tokenExpiration = this.extractTokenExpiration(storedToken);
        }
    }

    private extractTokenExpiration(token: string): Date {
        const decodedToken: any = jwtDecode(token);
        return new Date(decodedToken.exp * 1000);
    }

    clearToken() {
        this.tokenExpiration = undefined;
        localStorage.removeItem(this.tokenKey);
    }
}