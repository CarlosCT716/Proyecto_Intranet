import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../../core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { Horario } from '../../../../core/models/admin.interface';
import { delay } from 'rxjs';

@Component({
  selector: 'app-horarios-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, LoadingSpinnerComponent],
  templateUrl: './horarios.component.html'
})
export class HorariosComponent implements OnInit {
  private adminService = inject(AdminService);
  private cdr = inject(ChangeDetectorRef);
  
  horarios: Horario[] = [];
  horariosFiltrados: Horario[] = [];
  isLoading = true;
  searchTerm: string = '';

  ngOnInit() {
    this.cargarData();
  }

  cargarData() {
    this.isLoading = true;
    
    this.adminService.listarHorarios()
      .pipe(delay(2000))
      .subscribe({
        next: (res) => {
          this.horarios = res;
          this.filtrar();
          this.isLoading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.isLoading = false;
          this.cdr.detectChanges();
        }
      });
  }

  filtrar() {
    const term = this.searchTerm.toLowerCase();
    this.horariosFiltrados = this.horarios.filter(h => 
      h.nombreCurso?.toLowerCase().includes(term) || 
      h.diaSemana?.toLowerCase().includes(term) ||
      h.nombreAula?.toLowerCase().includes(term)
    );
  }

  toggleEstado(horario: Horario) {
    if(!horario.idHorario) return;
    
    const nuevoEstado = !horario.activo;
    const confirmacion = confirm(`¿Cambiar estado del horario de ${horario.nombreCurso}?`);

    if (confirmacion) {
        this.adminService.cambiarEstadoHorario(horario.idHorario).subscribe({
            next: () => {
                horario.activo = nuevoEstado;
                this.cdr.detectChanges();
            },
            error: () => alert('Error al cambiar el estado')
        });
    }
  }
}