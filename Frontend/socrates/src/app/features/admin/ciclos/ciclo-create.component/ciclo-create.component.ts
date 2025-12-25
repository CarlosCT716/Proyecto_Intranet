import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AdminService } from '../../../../../app/core/services/admin.service';

@Component({
  selector: 'app-ciclo-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './ciclo-create.component.html'
})
export class CicloCreateComponent {
  private fb = inject(FormBuilder);
  private adminService = inject(AdminService);
  private router = inject(Router);

  isSubmitting = false;

  form = this.fb.group({
    nombreCiclo: ['', [Validators.required, Validators.maxLength(20)]]
  });

  onSubmit() {
    if (this.form.invalid) return;

    this.isSubmitting = true;
    const data = this.form.value as any;

    this.adminService.crearCiclo(data).subscribe({
      next: () => {
        alert('Ciclo creado exitosamente');
        this.router.navigate(['/admin/ciclos']);
      },
      error: () => {
        alert('Error al crear ciclo');
        this.isSubmitting = false;
      }
    });
  }
}