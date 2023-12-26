import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take } from 'rxjs';
import { environment } from 'src/environments/environments';
import { CategoryDefault } from './category-default';

@Injectable({
  providedIn: 'root'
})
export class CategoryDefaultService {

  constructor(private http: HttpClient) { }

  sendNewCategory(obj: object) {
    return this.http.post(`${environment.baseUrl}/api/financas/categoryDefault`, obj).pipe(take(1));
  }

  findAll(): Observable<CategoryDefault[]> {
    return this.http.get<CategoryDefault[]>(`${environment.baseUrl}/api/financas/categoryDefault`);
  }
}
