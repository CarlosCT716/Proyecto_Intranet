import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../../../app/core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../../app/shared/loading-spinner.component';
import { Ciclo } from '../../../../../app/core/models/admin.interface';
import { delay } from 'rxjs';

@Component({
  selector: 'app-ciclos-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, LoadingSpinnerComponent],
  templateUrl: './ciclos.component.html'
})
export class CiclosComponent implements OnInit {
  private adminService = inject(AdminService);
  
  ciclos: Ciclo[] = [];
  ciclosFiltrados: Ciclo[] = [];
  isLoading = true;
  searchTerm: string = '';
  private cdr = inject(ChangeDetectorRef);

  ngOnInit() {
    this.adminService.listarCiclos().pipe(delay(2000)).subscribe({
      next: (res) => {
        this.ciclos = res;
        this.ciclosFiltrados = res;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => this.isLoading = false
    });
  }

  filtrar() {
    const term = this.searchTerm.toLowerCase();
    this.ciclosFiltrados = this.ciclos.filter(c => 
      c.nombreCiclo.toLowerCase().includes(term)
    );
  }
}