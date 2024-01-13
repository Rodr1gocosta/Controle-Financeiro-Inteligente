import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { MessageOperationService } from 'src/app/shared/util/message-operation/message-operation.service';
import { UserService } from '../user/user.service';

const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  
  loginForm!: FormGroup;
  resetPasswordForm!: FormGroup;

  state: boolean = true;
  messageResetPassword: boolean = false;

  constructor(
              private formBuilder: FormBuilder,
              private authService: AuthService,
              private router: Router,
              private message: MessageOperationService,
              private userService: UserService
  ) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.pattern(emailPattern)]],
      password: ['', Validators.required]
    });

    this.resetPasswordForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.pattern(emailPattern)]]
    })
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }
    
    const { username, password } = this.loginForm.value;
    this.authService.login(username, password).subscribe(response => {
      
      this.router.navigate(['']);
      this.message.message('Login efetuado com sucesso!', 'success');
    },
      error => {
        switch (error.status) {
          case 403: {
            this.message.message('Usuário inativo. Entre em contato com o suporte.', 'warn');
          } break;
        }
      }
    );
  }

  resetPassword() {
    if(this.resetPasswordForm.invalid) {
      return;
    }

    this.authService.clearToken();
    this.userService.resetPassword(this.resetPasswordForm.value).subscribe(response => {

      this.messageResetPassword = true;
      this.message.message("Email enviado com as instruções de recriar a senha", 'success');
    })
  }

  redirectResetPassword() {
    this.state = false;
    this.messageResetPassword = false;
  }

  backLogin() {
    this.state = true;
  }

  errorValidUsername() {
    if (this.loginForm.get('username')?.hasError('required')) {
      return 'Campo obrigatório!';
    } else if (this.loginForm.get('username')?.hasError('pattern')) {
      return 'E-mail inválido';
    }
    return false;
  }

  errorValidPassword() {
    if (this.loginForm.get('password')?.hasError('required')) {
      return 'Senha é obrigatória!';
    }
    return false;
  }
}
