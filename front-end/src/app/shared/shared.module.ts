import { NgModule } from "@angular/core";
import { ConfirmationDialogComponent } from './util/confirmation-dialog/confirmation-dialog.component';
import { ConfirmationDialogService } from "./util/confirmation-dialog/confirmation-dialog.service";
import { MatDialogModule } from "@angular/material/dialog";
import { MessageOperationService } from "./util/message-operation/message-operation.service";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatButtonModule } from "@angular/material/button";

@NgModule({
    declarations: [
        ConfirmationDialogComponent,
    ],
    imports: [
        MatDialogModule,
        MatSnackBarModule,
        MatButtonModule,
    ],
    providers: [
        ConfirmationDialogService,
        MessageOperationService
    ],
  })
  export class SharedModule {}