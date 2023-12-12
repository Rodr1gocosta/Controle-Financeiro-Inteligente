import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { TipoCategory } from 'src/app/shared/enum/tipo-category';

@Component({
  selector: 'app-crud-category-default',
  templateUrl: './crud-category-default.component.html',
  styleUrls: ['./crud-category-default.component.scss']
})
export class CrudCategoryDefaultComponent {

  essencial = TipoCategory.ESSENCIAL;
  naoEssencial = TipoCategory.NAO_ESSENCIAL;

  form = this.formBuilder.group({
    name: ['', Validators.required],
    typeCategory: ['', Validators.required]
  });

  constructor(private formBuilder: FormBuilder,
              public dialogRef: MatDialogRef<CrudCategoryDefaultComponent>) { }

  createCategory() {
    if(this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}
