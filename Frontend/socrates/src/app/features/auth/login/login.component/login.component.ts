import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import Swal from 'sweetalert2';

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

  showPassword: boolean = false;
  isLoading: boolean = false;

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

    Swal.fire({
      title: 'Iniciando sesión',
      text: 'Verificando credenciales...',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });

    const credentials = {
      username: this.loginForm.value.username!,
      password: this.loginForm.value.password!
    };

    this.authService.login(credentials).subscribe({
      next: (userData) => {
        Swal.close();
        this.isLoading = false;
        
        const Toast = Swal.mixin({
          toast: true,
          position: 'top-end',
          showConfirmButton: false,
          timer: 2000,
          timerProgressBar: true,
          didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
          }
        });
        
        Toast.fire({
          icon: 'success',
          title: `Bienvenido`
        });

        this.redirectByRole(userData.rol);
      },
      error: (err) => {
        console.error(err);
        this.isLoading = false;

        Swal.fire({
          icon: 'error',
          title: 'Credenciales Incorrectas',
          text: 'El usuario o la contraseña no coinciden.',
          confirmButtonColor: '#0B4D6C'
        });
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