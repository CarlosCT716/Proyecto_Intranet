import { Component, inject, OnInit,ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../../../app/core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { Aula } from '../../../../core/models/admin.interface';
import { delay } from 'rxjs';

@Component({
  selector: 'app-aulas-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, LoadingSpinnerComponent],
  templateUrl: './aulas.component.html'
})
export class AulasComponent implements OnInit {
  private adminService = inject(AdminService);
  
  aulas: Aula[] = [];
  aulasFiltradas: Aula[] = [];
  isLoading = true;
  searchTerm: string = '';
  private cdr = inject(ChangeDetectorRef);

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
  }

  toggleEstado(aula: Aula) {
    if(!aula.idAula) return;
    
    const nuevoEstado = !aula.activo;
    const confirmacion = confirm(`¿Estás seguro de ${nuevoEstado ? 'activar' : 'desactivar'} el aula ${aula.descripcion}?`);

    if (confirmacion) {
        this.adminService.cambiarEstadoAula(aula.idAula).subscribe({
            next: () => {
                aula.activo = nuevoEstado;
                 this.cdr.detectChanges();
            },
            error: () => alert('Error al cambiar el estado del aula')
        });
    }
  }
}