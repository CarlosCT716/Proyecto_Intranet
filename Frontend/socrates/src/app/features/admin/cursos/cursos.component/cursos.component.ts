import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../../core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { delay } from 'rxjs';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-cursos-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, LoadingSpinnerComponent],
  templateUrl: './cursos.component.html'
})
export class CursosComponent implements OnInit {
  private adminService = inject(AdminService);
  private cdr = inject(ChangeDetectorRef);
  
  protected readonly Math = Math;

  cursos: any[] = [];
  cursosFiltrados: any[] = [];
  isLoading = true;
  searchTerm: string = '';

  currentPage: number = 1;
  itemsPerPage: number = 10;

  ngOnInit() {
    this.cargarData();
  }

  cargarData() {
    this.isLoading = true;
    this.cdr.detectChanges();
    
    this.adminService.listarCursos().pipe(delay(2000)).subscribe({
      next: (res) => {
        this.cursos = res;
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
          text: 'No se pudieron cargar los cursos',
          confirmButtonColor: '#d33'
        });
      }
    });
  }

  filtrar() {
    const term = this.searchTerm.toLowerCase();
    this.cursosFiltrados = this.cursos.filter(c => 
      c.nombreCurso?.toLowerCase().includes(term) || 
      c.nombreCarrera?.toLowerCase().includes(term) ||
      c.nombreProfesor?.toLowerCase().includes(term)
    );
    this.currentPage = 1;
  }

  get paginatedCursos(): any[] {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    return this.cursosFiltrados.slice(start, end);
  }

  get totalPages(): number {
    return Math.ceil(this.cursosFiltrados.length / this.itemsPerPage);
  }

  changePage(page: number) {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }

  toggleEstado(curso: any) {
    if (!curso.idCurso) return;

    const nuevoEstado = !curso.activo;

    Swal.fire({
      title: '¿Estás seguro?',
      text: `¿Deseas ${nuevoEstado ? 'activar' : 'desactivar'} el curso ${curso.nombreCurso}?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#0B4D6C',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, cambiar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.adminService.cambiarEstadoCurso(curso.idCurso).subscribe({
          next: () => {
            curso.activo = nuevoEstado;
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
              title: `Curso ${nuevoEstado ? 'activado' : 'desactivado'} correctamente`
            });
          },
          error: () => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'Error al cambiar el estado del curso',
              confirmButtonColor: '#d33'
            });
          }
        });
      }
    });
  }
}