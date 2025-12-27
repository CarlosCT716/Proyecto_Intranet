import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../../core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { Horario } from '../../../../core/models/admin.interface';
import { delay } from 'rxjs';
import Swal from 'sweetalert2';
import { NgxPaginationModule } from 'ngx-pagination';

@Component({
  selector: 'app-horarios-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, LoadingSpinnerComponent, NgxPaginationModule],
  templateUrl: './horarios.component.html',
  styleUrl: './horarios.component.css'
})
export class HorariosComponent implements OnInit {
  private adminService = inject(AdminService);
  private cdr = inject(ChangeDetectorRef);
  
  horarios: Horario[] = [];
  horariosFiltrados: Horario[] = [];
  isLoading = true;
  searchTerm: string = '';
  p: number = 1;
  itemsPerPage: number = 10;

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
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudieron cargar los horarios',
            confirmButtonColor: '#d33'
          });
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
    this.p = 1;
  }

  toggleEstado(horario: Horario) {
    if(!horario.idHorario) return;
    
    const idHorario = horario.idHorario;
    const nuevoEstado = !horario.activo;
    
    Swal.fire({
      title: '¿Estás seguro?',
      text: `¿Deseas ${nuevoEstado ? 'activar' : 'desactivar'} el horario de ${horario.nombreCurso}?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#0B4D6C',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, cambiar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.adminService.cambiarEstadoHorario(idHorario).subscribe({
          next: () => {
            horario.activo = nuevoEstado;
            this.cdr.detectChanges();
            
            const Toast = Swal.mixin({
              toast: true,
              position: 'top-end',
              showConfirmButton: false,
              timer: 3000,
              timerProgressBar: true
            });
            Toast.fire({
              icon: 'success',
              title: `Horario ${nuevoEstado ? 'activado' : 'desactivado'} correctamente`
            });
          },
          error: () => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'Error al cambiar el estado del horario',
              confirmButtonColor: '#d33'
            });
          }
        });
      }
    });
  }
}