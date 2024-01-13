import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take } from 'rxjs';
import { environment } from 'src/environments/environments';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  findAll(): Observable<User[]> {
    return this.http.get<User[]>(`${environment.baseUrl}/api/security/user`);
  }

  sendNewUser(obj: object) {
    return this.http.post(`${environment.baseUrl}/api/security/user`, obj).pipe(take(1));
  }

  sendNewPassword(obj: object) {
    return this.http.put(`${environment.baseUrl}/api/security/user/password`, obj).pipe(take(1));
  }

  resetPassword(obj: object) {
    return this.http.post(`${environment.baseUrl}/api/security/user/resetPassword`, obj).pipe(take(1));
  }

}
