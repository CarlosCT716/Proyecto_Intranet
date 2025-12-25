import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../../../app/core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../../app/shared/loading-spinner.component';
import { Carrera } from '../../../../core/models/admin.interface';
import { delay } from 'rxjs';

@Component({
  selector: 'app-carreras-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, LoadingSpinnerComponent],
  templateUrl: './carreras.component.html'
})
export class CarrerasComponent implements OnInit {
  private adminService = inject(AdminService);
  
  carreras: Carrera[] = [];
  carrerasFiltradas: Carrera[] = [];
  isLoading = true;
  searchTerm: string = '';
  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.adminService.listarCarreras().pipe(delay(2000)).subscribe({
      next: (res) => {
        this.carreras = res;
        this.carrerasFiltradas = res;
        this.isLoading = false;

        this.cdr.detectChanges();
      },
      error: () => this.isLoading = false
    });
  }

  filtrar() {
    const term = this.searchTerm.toLowerCase();
    this.carrerasFiltradas = this.carreras.filter(c => 
      c.nombreCarrera.toLowerCase().includes(term) || 
      c.descripcion.toLowerCase().includes(term)
    );
  }
}