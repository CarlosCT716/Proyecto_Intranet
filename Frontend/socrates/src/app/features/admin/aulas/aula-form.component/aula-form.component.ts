import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { AdminService } from '../../../../core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { delay } from 'rxjs';

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
          alert('Error al cargar el aula');
          this.router.navigate(['/admin/aulas']);
          this.cdr.detectChanges();
        }
      });
  }

  actualizarAforoActual() {
    if (!this.isEditMode && this.aula.aforoMaximo) {
        this.aula.aforoActual = this.aula.aforoMaximo;
    }
  }

  guardar() {
    this.isLoading = true;
    const request$ = this.isEditMode 
        ? this.adminService.actualizarAula(this.aula.idAula, this.aula)
        : this.adminService.crearAula(this.aula);

    request$.subscribe({
        next: () => {
          alert(this.isEditMode ? 'Aula actualizada' : 'Aula creada');
          this.router.navigate(['/admin/aulas']);
        },
        error: () => {
          this.isLoading = false;
          alert('Error al guardar');
          this.cdr.detectChanges();
        }
    });
  }
}