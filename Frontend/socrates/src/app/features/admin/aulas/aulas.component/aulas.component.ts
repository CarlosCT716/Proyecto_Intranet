import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../../../app/core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { Aula } from '../../../../core/models/admin.interface';
import { delay } from 'rxjs';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-aulas-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, LoadingSpinnerComponent],
  templateUrl: './aulas.component.html'
})
export class AulasComponent implements OnInit {
  private adminService = inject(AdminService);
  private cdr = inject(ChangeDetectorRef);
  
  protected readonly Math = Math;

  aulas: Aula[] = [];
  aulasFiltradas: Aula[] = [];
  isLoading = true;
  searchTerm: string = '';

  currentPage: number = 1;
  itemsPerPage: number = 10;

  ngOnInit() {
    this.cargarData();
  }

  cargarData() {
    this.isLoading = true;
    this.adminService.listarAulas().pipe(delay(2000)).subscribe({
      next: (res) => {
        this.aulas = res;
        this.filtrar(); 
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => this.isLoading = false
    });
  }

  filtrar() {
    const term = this.searchTerm.toLowerCase();
    this.aulasFiltradas = this.aulas.filter(a => 
      a.descripcion.toLowerCase().includes(term)
    );
    this.currentPage = 1;
  }

  get paginatedAulas(): Aula[] {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    return this.aulasFiltradas.slice(start, end);
  }

  get totalPages(): number {
    return Math.ceil(this.aulasFiltradas.length / this.itemsPerPage);
  }

  changePage(page: number) {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }

  toggleEstado(aula: Aula) {
    if(!aula.idAula) return;
    
    const idAula = aula.idAula;
    const nuevoEstado = !aula.activo;
    
    Swal.fire({
      title: '¿Estás seguro?',
      text: `¿Deseas ${nuevoEstado ? 'activar' : 'desactivar'} el aula ${aula.descripcion}?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#0B4D6C',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, cambiar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.adminService.cambiarEstadoAula(idAula).subscribe({
          next: () => {
            aula.activo = nuevoEstado;
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
              title: `Aula ${nuevoEstado ? 'activada' : 'desactivada'} correctamente`
            });
          },
          error: () => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'Error al cambiar el estado del aula',
              confirmButtonColor: '#d33'
            });
          }
        });
      }
    });
  }
}