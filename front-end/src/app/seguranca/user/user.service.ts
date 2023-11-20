import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take } from 'rxjs';
import { environment } from 'src/environments/environments';
import { User } from './user';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient,
    private snack: MatSnackBar) { }

  findAll(): Observable<User[]> {
    return this.http.get<User[]>(`${environment.baseUrl}/api/security/user`);
  }

  sendNewUser(obj: object) {
    return this.http.post(`${environment.baseUrl}/api/security/user`, obj).pipe(take(1));
  }

  message(msg: String) {
    this.snack.open(`${msg}`, 'OK', {
      horizontalPosition: 'end',
      verticalPosition: 'top',
      duration: 3000
    })
  }

}
