import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { MessageOperationService } from 'src/app/shared/util/message-operation/message-operation.service';

const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  
  loginForm!: FormGroup;

  constructor(
              private formBuilder: FormBuilder,
              private authService: AuthService,
              private router: Router,
              private message: MessageOperationService
  ) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.pattern(emailPattern)]],
      password: ['', Validators.required]
    });
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
