import { Component, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AdminService } from '../../../../../app/core/services/admin.service';
import { delay } from 'rxjs';

@Component({
  selector: 'app-carrera-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './carrera-create.component.html'
})
export class CarreraCreateComponent {
  private fb = inject(FormBuilder);
  private adminService = inject(AdminService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  isSubmitting = false;

  form = this.fb.group({
    nombreCarrera: ['', [Validators.required, Validators.minLength(5)]],
    descripcion: ['', [Validators.required, Validators.maxLength(255)]]
  });

  onSubmit() {
    if (this.form.invalid) return;

    this.isSubmitting = true;
    const data = this.form.value as any;

    this.adminService.crearCarrera(data).pipe(delay(2000)).subscribe({
      next: () => {
        alert('Carrera creada exitosamente');
        this.cdr.detectChanges();
        this.router.navigate(['/admin/carreras']);
      },
      error: () => {
        alert('Error al crear carrera');
        this.isSubmitting = false;
      }
    });
  }
}