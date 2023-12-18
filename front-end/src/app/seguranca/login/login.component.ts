import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';

const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  
  loginForm!: FormGroup;
  hide = true;

  constructor(
              private formBuilder: FormBuilder,
              private authService: AuthService,
              private router: Router
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

    this.router.navigate(['']);

    const { username, password } = this.loginForm.value;
    this.authService.login(username, password).subscribe(response => {
      
      this.router.navigate(['']);
      this.authService.message('Login efetuado com sucesso!', 'success');
    },
      error => {
        let errorMessage = 'Ocorreu um erro ao fazer login. Por favor, tente novamente mais tarde.';

        switch (error.status) {
          case 401: {
            errorMessage = 'Credenciais inválidas. Verifique seu nome de usuário e senha.';
          } break;
          case 403: {
            errorMessage = 'Usuário inativo. Entre em contato com o suporte.';
          } break;
        }

        this.authService.message(errorMessage);
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
