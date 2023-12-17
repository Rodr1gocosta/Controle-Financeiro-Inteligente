import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable, take } from 'rxjs';
import { Planning } from 'src/app/shared/model/planning';
import { environment } from 'src/environments/environments';

@Injectable({
  providedIn: 'root'
})
export class FinanceiroService {

  constructor(private http: HttpClient,
              private snack: MatSnackBar) { }

  sendNewPlanning(obj: object) {
    return this.http.post(`${environment.baseUrl}/api/financas/planning`, obj).pipe(take(1));
  }

  findByDate(month: number, year: number): Observable<Planning> {
    return this.http.get<Planning>(`${environment.baseUrl}/api/financas/planning/${month}/${year}`);
  }

  message(msg: String) {
    this.snack.open(`${msg}`, 'OK', {
        horizontalPosition: 'end',
        verticalPosition: 'top',
        duration: 3000
    })
}
}
