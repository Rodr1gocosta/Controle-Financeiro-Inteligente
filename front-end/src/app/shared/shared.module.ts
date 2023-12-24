import { NgModule } from "@angular/core";

import { MatDialogModule } from "@angular/material/dialog";
import { MessageOperationService } from "./util/message-operation/message-operation.service";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatButtonModule } from "@angular/material/button";

import { ProgressSpinnerService } from "./util/progress-spinner/progress-spinner.service";
import { ConfirmationDialogService } from "./util/confirmation-dialog/confirmation-dialog.service";

import { ConfirmationDialogComponent } from './util/confirmation-dialog/confirmation-dialog.component';
import { CommonModule } from "@angular/common";

@NgModule({
    declarations: [
        ConfirmationDialogComponent,
    ],
    imports: [
        CommonModule,
        MatDialogModule,
        MatSnackBarModule,
        MatButtonModule,
    ],
    providers: [
        ConfirmationDialogService,
        MessageOperationService,
        ProgressSpinnerService
    ],
  })
  export class SharedModule {}