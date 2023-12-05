import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class FinanceiroService {

  constructor(private snack: MatSnackBar) { }

  message(msg: String) {
    this.snack.open(`${msg}`, 'OK', {
        horizontalPosition: 'end',
        verticalPosition: 'top',
        duration: 3000
    })
}
}
