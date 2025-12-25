import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../../../../core/services/admin.service';
import { LoadingSpinnerComponent } from '../../../../shared/loading-spinner.component';
import { Auditoria } from '../../../../core/models/admin.interface';
import { delay } from 'rxjs';

@Component({
  selector: 'app-audit-list',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingSpinnerComponent],
  templateUrl: './auditoria.component.html'
})
export class AuditoriaComponent implements OnInit {
  private adminService = inject(AdminService);
  private cdr = inject(ChangeDetectorRef);

  logs: Auditoria[] = [];
  logsFiltrados: Auditoria[] = [];
  isLoading = true;
  searchTerm: string = '';

  ngOnInit() {
    this.cargarData();
  }

  cargarData() {
    this.isLoading = true;
    this.adminService.listarAuditoria().pipe(delay(500)).subscribe({
      next: (res) => {
        this.logs = res;
        this.filtrar();
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => this.isLoading = false
    });
  }

  filtrar() {
    const term = this.searchTerm.toLowerCase();
    this.logsFiltrados = this.logs.filter(log => 
      (log.nombreUsuario || 'sistema').toLowerCase().includes(term) ||
      log.accion.toLowerCase().includes(term) ||
      log.tablaAfectada?.toLowerCase().includes(term)
    );
  }

  getBadgeClass(accion: string): string {
    if(!accion) return '';
    const acc = accion.toUpperCase();
    if (acc.includes('CREACION')) return 'bg-green-100 text-green-800 border-green-200';
    if (acc.includes('ACTUALIZACION') || acc.includes('MODIFICACION')) return 'bg-blue-100 text-blue-800 border-blue-200';
    if (acc.includes('ELIMINACION')) return 'bg-red-100 text-red-800 border-red-200';
    return 'bg-gray-100 text-gray-800 border-gray-200';
  }
}