import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AdminService } from '../../../../core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { delay } from 'rxjs';

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
      .pipe(delay(2000))
      .subscribe({
        next: (res) => {
          this.carrera = res;
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.isLoading = false;
          alert('Error al cargar la carrera');
          this.router.navigate(['/admin/carreras']);
          this.cdr.detectChanges();
        }
      });
  }

  guardar() {
    this.isLoading = true;
    this.cdr.detectChanges();

    const request$ = this.isEditMode
        ? this.adminService.actualizarCarrera(this.carrera.idCarrera, this.carrera)
        : this.adminService.crearCarrera(this.carrera);

    request$.subscribe({
        next: () => {
          alert(this.isEditMode ? 'Carrera actualizada' : 'Carrera creada');
          this.router.navigate(['/admin/carreras']);
        },
        error: () => {
          this.isLoading = false;
          alert('Error al guardar');
          this.cdr.detectChanges();
        }
    });
  }
}