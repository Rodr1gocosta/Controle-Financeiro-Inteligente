import { Injectable } from "@angular/core";
import { MatSnackBar } from "@angular/material/snack-bar";

@Injectable({
    providedIn: 'root'
})
export class MessageOperationService {

    constructor(private snack: MatSnackBar) { }

    message(msg: String, color: string = 'default') {
        this.snack.open(`${msg}`, 'OK', {
            horizontalPosition: 'end',
            verticalPosition: 'top',
            duration: 1000,
            panelClass: [color]
        })
    }
}