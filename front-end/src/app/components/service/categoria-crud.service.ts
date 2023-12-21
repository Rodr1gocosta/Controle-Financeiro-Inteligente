import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { take } from 'rxjs';
import { environment } from 'src/environments/environments';

@Injectable({
  providedIn: 'root'
})
export class CategoriaCrudService {

  constructor(private http: HttpClient) { }

  sendNewListCategorias(obj: object, planningId: string) {
    return this.http.post(`${environment.baseUrl}/api/financas/categories/${planningId}`, obj).pipe(take(1));
  }
}
