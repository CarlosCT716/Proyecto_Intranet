import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../../core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { Pago } from '../../../../core/models/admin.interface';
import { delay } from 'rxjs';

@Component({
  selector: 'app-pagos-list',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingSpinnerComponent],
  templateUrl: './pagos.component.html'
})
export class PagosComponent implements OnInit {
  private adminService = inject(AdminService);
  private cdr = inject(ChangeDetectorRef);

  pagos: Pago[] = [];
  pagosFiltrados: Pago[] = [];
  isLoading = true;
  searchTerm: string = '';
  filtroEstado: string = 'TODOS'; 

  ngOnInit() {
    this.cargarData();
  }

  cargarData() {
    this.isLoading = true;
    this.adminService.listarPagos().pipe(delay(1000)).subscribe({
      next: (res) => {
        this.pagos = res;
        console.log(this.pagos);
        this.filtrar();
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => this.isLoading = false
    });
  }

  filtrar() {
    const term = this.searchTerm.toLowerCase();
    
    this.pagosFiltrados = this.pagos.filter(p => {
      const nombreAlumno = p.matricula?.alumno ? 
        `${p.matricula.alumno.nombres} ${p.matricula.alumno.apellidos}`.toLowerCase() : '';
      
      const coincideTexto = nombreAlumno.includes(term) || 
                            p.concepto.toLowerCase().includes(term);

      const coincideEstado = this.filtroEstado === 'TODOS' || 
                             p.estadoPago.descripcion.toUpperCase() === this.filtroEstado;

      return coincideTexto && coincideEstado;
    });
  }


}