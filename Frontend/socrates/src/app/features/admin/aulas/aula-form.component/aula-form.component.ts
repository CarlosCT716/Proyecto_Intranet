import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AdminService } from '../../../../core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { delay } from 'rxjs';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-aula-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, LoadingSpinnerComponent],
  templateUrl: './aula-form.component.html'
})
export class AulaFormComponent implements OnInit {
  private adminService = inject(AdminService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);

  isLoading = false;
  isEditMode = false;
  titulo = 'Nueva Aula';

  aula: any = {
    descripcion: '',
    aforoMaximo: 30,
    aforoActual: 30,
    activo: true
  };

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.isEditMode = true;
      this.titulo = 'Editar Aula';
      this.cargarAula(id);
    }
  }

  cargarAula(id: number) {
    this.isLoading = true;
    this.adminService.obtenerAula(id)
      .pipe(delay(2000))
      .subscribe({
        next: (res) => {
          this.aula = res;
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.isLoading = false;
          this.cdr.detectChanges();
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Error al cargar la información del aula',
            confirmButtonColor: '#d33'
          }).then(() => {
            this.router.navigate(['/admin/aulas']);
          });
        }
      });
  }

  actualizarAforoActual() {
    if (!this.isEditMode && this.aula.aforoMaximo) {
        this.aula.aforoActual = this.aula.aforoMaximo;
    }
  }

  guardar() {
    Swal.fire({
      title: this.isEditMode ? 'Actualizando...' : 'Guardando...',
      text: 'Por favor espere',
      allowOutsideClick: false,
      didOpen: () => {
        Swal.showLoading();
      }
    });

    this.isLoading = true;
    this.cdr.detectChanges();

    const request$ = this.isEditMode 
        ? this.adminService.actualizarAula(this.aula.idAula, this.aula)
        : this.adminService.crearAula(this.aula);

    request$.subscribe({
        next: () => {
          Swal.fire({
            icon: 'success',
            title: 'Éxito',
            text: this.isEditMode ? 'Aula actualizada correctamente' : 'Aula creada correctamente',
            confirmButtonColor: '#0B4D6C'
          }).then(() => {
            this.router.navigate(['/admin/aulas']);
          });
        },
        error: () => {
          this.isLoading = false;
          this.cdr.detectChanges();
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Hubo un problema al guardar los cambios',
            confirmButtonColor: '#d33'
          });
        }
    });
  }
}