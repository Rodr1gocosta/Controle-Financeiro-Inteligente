import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { User } from '../user';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;

@Component({
  selector: 'app-user-crud',
  templateUrl: './user-crud.component.html',
  styleUrls: ['./user-crud.component.scss']
})
export class UserCrudComponent {

  form!: FormGroup;

  constructor(@Inject(MAT_DIALOG_DATA)
              public data: User,
              public dialogRef: MatDialogRef<UserCrudComponent>,
              private formBuilder: FormBuilder,
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      name: ['', Validators.required],
      username: ['', [Validators.required, Validators.pattern(emailPattern)]],
      cpf: ['', Validators.required],
      phoneNumber: ['', Validators.required]

    });
  }

  onSubmit() {
    if (!this.form.invalid) {
      this.data.name = this.form.value.name;
      this.data.username = this.form.value.username;
      this.data.cpf = this.form.value.cpf;
      this.data.phoneNumber = this.form.value.phoneNumber;

      this.dialogRef.close(this.data);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  errorValidUsername() {
    if (this.form.get('username')?.hasError('required')) {
      return 'Campo obrigatório!';
    } else if (this.form.get('username')?.hasError('pattern')) {
      return 'E-mail inválido';
    }
    return false;
  }

  errorRequiredField(controlName: string) {
    const control = this.form.get(controlName);

    if (control?.hasError('required')) {
      return 'Campo é obrigatório!';
    }
    return 'Campo inválido!';
  }

}
