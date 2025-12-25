import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AdminService } from '../../../../core/services/admin.service';

@Component({
  selector: 'app-aula-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './aulas-create.component.html'
})
export class AulasCreateComponent {
  private fb = inject(FormBuilder);
  private adminService = inject(AdminService);
  private router = inject(Router);

  isSubmitting = false;

form = this.fb.group({
    descripcion: ['', [Validators.required, Validators.maxLength(50)]],
    aforoMaximo: [30, [Validators.required, Validators.min(1), Validators.max(30)]], 
    aforoActual: [30, [Validators.required, Validators.min(0), Validators.max(30)]], 
    activo: [true]
  });
  actualizarAforoActual() {
    const max = this.form.get('aforoMaximo')?.value;
    if (max) {
        this.form.get('aforoActual')?.setValue(max);
    }
  }

  onSubmit() {
    if (this.form.invalid) return;

    this.isSubmitting = true;
    const data = this.form.value as any;

    this.adminService.crearAula(data).subscribe({
      next: () => {
        alert('Aula registrada exitosamente');
        this.router.navigate(['/admin/aulas']);
      },
      error: () => {
        alert('Error al registrar aula');
        this.isSubmitting = false;
      }
    });
  }
}