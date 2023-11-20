import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Router } from "@angular/router";
import { Observable, map } from "rxjs";
import { environment } from "src/environments/environments";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    baseUrl: String = environment.baseUrl;

    constructor(private http: HttpClient,
        private snack: MatSnackBar,
        private router: Router
    ) { }

    login(username: string, password: string): Observable<any> {
        const url = this.baseUrl + "/api/security/authenticate";

        return this.http.post<any>(url, { username, password }).pipe(
            map((response: any) => {
                this.saveToken(response.token);
            })
        );
    }

    logout() {
        localStorage.clear();
        this.router.navigate(['/login']);
    }

    private saveToken(token: string) {
        localStorage.setItem('token', token);
    }

    isAuthenticated(): boolean {
        const token = localStorage.getItem('token');

        // precisa implementar isTokenExpired
        return token !== null;
    }

    private isTokenExpired(token: string) {
        // Implemente a verificação de validade do token, se necessário
        // Retorna true se o token estiver expirado, caso contrário, retorna false
        // Por exemplo, você pode verificar a data de expiração do token aqui
    }

    message(msg: String) {
        this.snack.open(`${msg}`, 'OK', {
            horizontalPosition: 'end',
            verticalPosition: 'top',
            duration: 3000
        })
    }
 }