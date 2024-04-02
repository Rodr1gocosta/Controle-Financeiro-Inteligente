import { HttpClient, HttpHeaders } from '@angular/common/http';
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

  deletePlanning(planningId: string): Observable<void> {
    return this.http.delete<void>(`${environment.baseUrl}/api/financas/planning/${planningId}`);
  }

  deleteCategories(categoriesIdList: any[], planningId: string): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      body: categoriesIdList
    };

    return this.http.delete(`${environment.baseUrl}/api/financas/categories/${planningId}`, httpOptions);
  }

  onDownloadPDF(planningId: string): Observable<Blob> {
    return this.http.get(`${environment.baseUrl}/api/financas/planning/download/${planningId}`, { 
      responseType: 'blob',
    });
  }

}
