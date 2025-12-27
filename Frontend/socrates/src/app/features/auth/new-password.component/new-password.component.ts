import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { LoadingSpinnerComponent } from '../../../shared/loading-spinner.component'; 
import Swal from 'sweetalert2';

@Component({
  selector: 'app-new-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, LoadingSpinnerComponent],
  templateUrl: './new-password.component.html'
})
export class NewPasswordComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  usuario: string = '';
  nuevaContrasena: string = '';
  confirmarContrasena: string = '';
  
  isLoading = false;
  showPassword = false;

  toggleVisibility() {
    this.showPassword = !this.showPassword;
  }

  onSubmit() {
    if (!this.usuario.trim() || !this.nuevaContrasena.trim()) {
      Swal.fire('Error', 'Todos los campos son obligatorios', 'warning');
      return;
    }

    if (this.nuevaContrasena !== this.confirmarContrasena) {
      Swal.fire('Error', 'Las contraseñas no coinciden', 'error');
      return;
    }

    if (this.nuevaContrasena.length < 6) {
      Swal.fire('Error', 'La contraseña debe tener al menos 6 caracteres', 'warning');
      return;
    }

    this.isLoading = true;
    const request = {
      usuario: this.usuario,
      nuevaContrasena: this.nuevaContrasena
    };

    this.authService.cambiarPassword(request).subscribe({
      next: () => {
        this.isLoading = false;
        
        Swal.fire({
          icon: 'success',
          title: '¡Éxito!',
          text: 'La contraseña ha sido actualizada correctamente.',
          confirmButtonColor: '#0B4D6C',
          allowOutsideClick: false 
        }).then((result) => {
          if (result.isConfirmed) {
            this.router.navigate(['/login']);
          }
        });
      },
      error: (err) => {
        this.isLoading = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: err.error?.mensaje || 'No se pudo actualizar la contraseña. Verifique el usuario.',
          confirmButtonColor: '#d33'
        });
      }
    });
  }

  limpiarFormulario() {
    this.usuario = '';
    this.nuevaContrasena = '';
    this.confirmarContrasena = '';
  }
}