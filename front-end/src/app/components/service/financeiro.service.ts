import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take } from 'rxjs';
import { Planning } from 'src/app/shared/model/planning';
import { environment } from 'src/environments/environments';

@Injectable({
  providedIn: 'root'
})
export class FinanceiroService {

  constructor(private http: HttpClient) { }

  sendNewPlanning(obj: object) {
    return this.http.post(`${environment.baseUrl}/api/financas/planning`, obj).pipe(take(1));
  }

  findByDate(month: number, year: number): Observable<Planning> {
    return this.http.get<Planning>(`${environment.baseUrl}/api/financas/planning/${month}/${year}`);
  }

  onDownloadPDF(planningId: string): Observable<Blob> {
    return this.http.get(`${environment.baseUrl}/api/financas/planning/download/${planningId}`, { 
      responseType: 'blob',
      reportProgress: true 
    });
  }
}
