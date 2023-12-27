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

    constructor(private http: HttpClient,
        private router: Router,
        private message: MessageOperationService
    ) {
        this.initializeTokenExpiration();
    }

    login(username: string, password: string): Observable<any> {
        const url = this.baseUrl + "/api/security/authenticate";

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
        const decodedToken: any = jwtDecode(token);
        this.tokenExpiration = new Date(decodedToken.exp * 1000);

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

    private clearToken() {
        this.tokenExpiration = undefined;
        localStorage.removeItem(this.tokenKey);
    }
}