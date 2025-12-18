import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
 selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  errorMessage: string = '';
  isLoading: boolean = false;
  showPassword: boolean = false;

  loginForm = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]]
  });

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
  onSubmit() {
    if (this.loginForm.invalid) return;

    this.isLoading = true;
    this.errorMessage = '';


    const credentials = {
      username: this.loginForm.value.username!,
      password: this.loginForm.value.password!
    };

    this.authService.login(credentials).subscribe({
      next: (userData) => {
        console.log('Login exitoso:', userData);
        this.redirectByRole(userData.rol);
      },
      error: (err) => {
        console.error('Error de login:', err);
        this.errorMessage = 'Usuario o contraseña incorrectos';
        this.isLoading = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }
private redirectByRole(rol: string) {
    switch (rol) {
      case 'ROLE_ADMIN':
        this.router.navigate(['/admin/dashboard']);
        break;
      case 'ROLE_PROFESOR': 
        this.router.navigate(['/docente/inicio']);
        break;
      case 'ROLE_ALUMNO':  
        this.router.navigate(['/estudiante/inicio']);
        break;
      default:
        this.router.navigate(['/']); 
    }
  }
}
