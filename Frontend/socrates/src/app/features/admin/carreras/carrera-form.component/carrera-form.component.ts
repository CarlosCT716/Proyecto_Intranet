import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AdminService } from '../../../../core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { delay } from 'rxjs';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-carrera-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, LoadingSpinnerComponent],
  templateUrl: './carrera-form.component.html'
})
export class CarreraFormComponent implements OnInit {
  private adminService = inject(AdminService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private cdr = inject(ChangeDetectorRef);

  isLoading = false;
  isEditMode = false;
  titulo = 'Nueva Carrera';

  carrera: any = {
    nombreCarrera: '',
    descripcion: ''
  };

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.isEditMode = true;
      this.titulo = 'Editar Carrera';
      this.cargarCarrera(id);
    }
  }

  cargarCarrera(id: number) {
    this.isLoading = true;
    this.adminService.obtenerCarrera(id)
      .pipe(delay(500))
      .subscribe({
        next: (res) => {
          this.carrera = res;
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.isLoading = false;
          this.cdr.detectChanges();
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Error al cargar la información de la carrera',
            confirmButtonColor: '#d33'
          }).then(() => {
            this.router.navigate(['/admin/carreras']);
          });
        }
      });
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

    const request$ = this.isEditMode
        ? this.adminService.actualizarCarrera(this.carrera.idCarrera, this.carrera)
        : this.adminService.crearCarrera(this.carrera);

    request$.subscribe({
        next: () => {
          Swal.fire({
            icon: 'success',
            title: 'Éxito',
            text: this.isEditMode ? 'Carrera actualizada correctamente' : 'Carrera creada correctamente',
            confirmButtonColor: '#0B4D6C'
          }).then(() => {
            this.router.navigate(['/admin/carreras']);
          });
        },
        error: () => {
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