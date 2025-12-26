import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../../core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';

@Component({
  selector: 'app-cursos-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, LoadingSpinnerComponent],
  templateUrl: './cursos.component.html'
})
export class CursosComponent implements OnInit {
  private adminService = inject(AdminService);
  
  cursos: any[] = [];
  cursosFiltrados: any[] = [];
  isLoading = true;
  searchTerm: string = '';

  ngOnInit() {
    this.cargarData();
  }

  cargarData() {
    this.isLoading = true;
    this.adminService.listarCursos().subscribe({
      next: (res) => {
        this.cursos = res;
        this.filtrar();
        this.isLoading = false;
      },
      error: () => this.isLoading = false
    });
  }

  filtrar() {
    const term = this.searchTerm.toLowerCase();
    this.cursosFiltrados = this.cursos.filter(c => 
      c.nombreCurso.toLowerCase().includes(term) || 
      c.nombreCarrera?.toLowerCase().includes(term) ||
      c.nombreProfesor?.toLowerCase().includes(term)
    );
  }

  toggleEstado(curso: any) {
    if (confirm(`¿Cambiar estado del curso ${curso.nombreCurso}?`)) {
      this.adminService.cambiarEstadoCurso(curso.idCurso).subscribe(() => {
        curso.activo = !curso.activo; 
      });
    }
  }
}