import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageOperationService } from 'src/app/shared/util/message-operation/message-operation.service';

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.scss']
})
export class PasswordResetComponent {
  token!: string | null;
  strength!: number;

  passwordResetForm!: FormGroup;

  confirm = false;
  showDetails = true;

  constructor(private route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private router: Router,
              private message: MessageOperationService) { }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token');

    this.passwordResetForm = this.formBuilder.group({
      token: [this.token, Validators.required],
      password: ['', Validators.required], 
      confirmPassword: ['', Validators.required]
    }, { validators: this.confirmPasswordValidator });
  }

  confirmPasswordValidator(control: AbstractControl) {
    return control.get('password')?.value === control.get('confirmPassword')?.value ? null : { mismatch: true};
  }

  createPassword() {
    if(this.passwordResetForm.value.password !== this.passwordResetForm.value.confirmPassword ) {
      this.message.message('As senhas não coincidem.', 'Error')
    }
    if(this.strength < 100) {
      this.message.message('A senha não segue a regra de segurança.', 'Error')
    }
  }

  onStrengthChanged(strength: number) {
    this.strength = strength;
    if(strength == 100) {
      this.confirm = true;
    } else {
      this.confirm = false;
    }
  }

}
